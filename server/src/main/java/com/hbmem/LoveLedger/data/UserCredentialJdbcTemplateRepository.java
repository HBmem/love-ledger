package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.data.mappers.UserCredentialMapper;
import com.hbmem.LoveLedger.model.UserCredential;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserCredentialJdbcTemplateRepository implements UserCredentialRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserCredentialJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public UserCredential findByUserId(int id) {
        final String sql = "SELECT * " +
                "FROM user_credential " +
                "WHERE id = ?;";

        UserCredential userCredential = jdbcTemplate.query(sql, new UserCredentialMapper(), id).stream()
                .findFirst()
                .orElse(null);

        if (userCredential != null) {
            userCredential.setRoles(findRolesByUserId(userCredential.getId()));
        }

        return userCredential;
    }

    @Override
    @Transactional
    public UserCredential findByEmail(String email) {
        final String sql = "SELECT * " +
                "FROM user_credential " +
                "WHERE email = ?;";

        UserCredential userCredential = jdbcTemplate.query(sql, new UserCredentialMapper(), email).stream()
                .findFirst()
                .orElse(null);

        if (userCredential != null) {
            userCredential.setRoles(findRolesByUserId(userCredential.getId()));
        }
        return userCredential;
    }

    @Override
    @Transactional
    public UserCredential add(UserCredential userCredential) {
        final String sql = "insert into user_credential(email, `password`, is_verified, is_disabled) "
                + "values(?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userCredential.getEmail());
            ps.setString(2, userCredential.getPassword());
            ps.setBoolean(3, userCredential.isVerified());
            ps.setBoolean(4, userCredential.isDisabled());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        userCredential.setId(keyHolder.getKey().intValue());

        updateRoles(userCredential);

        return userCredential;
    }

    @Override
    @Transactional
    public void update(UserCredential userCredential) {
        final String sql = "update user_credential set " +
                "password = ?, " +
                "is_verified = ?";

        jdbcTemplate.update(sql,
                userCredential.getPassword(),
                userCredential.isVerified());

        updateRoles(userCredential);
    }

    private List<String> findRolesByUserId(int userId) {
        final String sql = "select r.`name` " +
                "from user_role ur " +
                "inner join `role` r on ur.role_id = r.id " +
                "where ur.user_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"), userId);
    }
    private void updateRoles(UserCredential userCredential) {
        jdbcTemplate.update("delete from user_role where user_id = ?;", userCredential.getId());

        if (userCredential.getRoles() == null) {
            return;
        }

        for (String roleName : userCredential.getRoles()) {
            String sql = "insert into user_role (user_id, role_id) " +
                    "select ?, role_id from role where `name` = ?;";

            jdbcTemplate.update(sql, userCredential.getId(), roleName);
        }
    }
}
