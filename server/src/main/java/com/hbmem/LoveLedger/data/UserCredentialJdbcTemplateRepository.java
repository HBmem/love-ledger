package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.data.mappers.UserCredentialMapper;
import com.hbmem.LoveLedger.model.UserCredential;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;

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
            // TODO: Set Roles
        }

        return userCredential;
    }

    @Override
    @Transactional
    public UserCredential findByUsername(String username) {
        final String sql = "SELECT * " +
                "FROM user_credential " +
                "WHERE username = ?;";

        UserCredential userCredential = jdbcTemplate.query(sql, new UserCredentialMapper(), username).stream()
                .findFirst()
                .orElse(null);

        if (userCredential != null) {
            // TODO: Set Roles
        }

        return userCredential;
    }

    @Override
    @Transactional
    public UserCredential add(UserCredential userCredential) {
        final String sql = "insert into user_credential(username, email, `password`, is_verified, is_disabled) "
                + "values(?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userCredential.getUsername());
            ps.setString(2, userCredential.getEmail());
            ps.setString(3, userCredential.getPassword());
            ps.setBoolean(4, userCredential.isVerified());
            ps.setBoolean(5, userCredential.isDisabled());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        userCredential.setId(keyHolder.getKey().intValue());

        // TODO: Update Roles

        return userCredential;
    }

    @Override
    @Transactional
    public void update(UserCredential userCredential) {
        final String sql = "update user_credential set " +
                "password = ?, " +
                "email = ?, " +
                "is_verified = ?";

        jdbcTemplate.update(sql,
                userCredential.getPassword(),
                userCredential.getEmail(),
                userCredential.isVerified());

        // TODO: Update Roles
    }
}
