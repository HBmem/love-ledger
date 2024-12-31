package com.hbmem.LoveLedger.data.mappers;

import com.hbmem.LoveLedger.model.UserCredential;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCredentialMapper implements RowMapper<UserCredential> {
    @Override
    public UserCredential mapRow(ResultSet resultSet, int i) throws SQLException {
        UserCredential userCredential = new UserCredential();

        userCredential.setId(resultSet.getInt("id"));
        userCredential.setUsername(resultSet.getString("username"));
        userCredential.setPassword(resultSet.getString("password"));
        userCredential.setEmail(resultSet.getString("email"));
        userCredential.setDisabled(resultSet.getBoolean("is_disabled"));
        userCredential.setVerified(resultSet.getBoolean("is_verified"));

        return userCredential;
    }
}
