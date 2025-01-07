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
        final String sql = "INSERT INTO user_credential(email, `password`, phone_number, is_verified, is_disabled) "
                + "VALUES(?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userCredential.getEmail());
            ps.setString(2, userCredential.getPassword());
            ps.setString(3, userCredential.getPhoneNumber());
            ps.setBoolean(4, userCredential.isVerified());
            ps.setBoolean(5, userCredential.isDisabled());
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
    public boolean update(UserCredential userCredential) {
        final String sql = "UPDATE user_credential SET " +
                "password = ?, " +
                "phone_number = ?, " +
                "is_verified = ? " +
                "WHERE id = ?;";

        updateRoles(userCredential);

        return jdbcTemplate.update(sql,
                userCredential.getPassword(),
                userCredential.getPhoneNumber(),
                userCredential.isVerified(),
                userCredential.getId()) > 0;
    }

    @Override
    @Transactional
    public boolean disable(int userId) {
        final String sql = "UPDATE user_credential SET " +
                "is_disabled = ? " +
                "WHERE id = ?;";

        return jdbcTemplate.update(sql,
                true,
                userId) > 0;
    }

    private List<String> findRolesByUserId(int userId) {
        final String sql = "SELECT r.`name` " +
                "FROM user_role ur " +
                "INNER JOIN `role` r ON ur.role_id = r.id " +
                "WHERE ur.user_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"), userId);
    }
    private void updateRoles(UserCredential userCredential) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?;", userCredential.getId());

        if (userCredential.getRoles() == null) {
            return;
        }

        for (String roleName : userCredential.getRoles()) {
            String sql = "INSERT INTO user_role (user_id, role_id) " +
                    "SELECT ?, id FROM role WHERE `name` = ?;";

            jdbcTemplate.update(sql, userCredential.getId(), roleName);
        }
    }
}
