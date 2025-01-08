package com.hbmem.LoveLedger.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbmem.LoveLedger.data.mappers.UserProfileMapper;
import com.hbmem.LoveLedger.model.UserProfile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class UserProfileJdbcTemplateRepository implements UserProfileRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserProfileJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserProfile findByUserProfileId(int id) {
        final String sql = "SELECT * " +
                "FROM user_profile " +
                "WHERE id = ?;";

        return jdbcTemplate.query(sql, new UserProfileMapper(), id).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserProfile add(UserProfile userProfile) throws JsonProcessingException {
        final String sql = "INSERT INTO user_profile(first_name, last_name, gender, birthday, likes, dislikes, photo_url) " +
                "VALUES(?,?,?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userProfile.getFirstName());
            ps.setString(2, userProfile.getLastName());
            ps.setString(3, userProfile.getGender().toString());
            ps.setDate(4, userProfile.getBirthday() != null ? Date.valueOf(userProfile.getBirthday()) : null);

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                ps.setString(5, objectMapper.writeValueAsString(userProfile.getLikes()));
                ps.setString(6, objectMapper.writeValueAsString(userProfile.getDislikes()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            ps.setString(7, userProfile.getPhotoUrl());
            return ps;
        }, keyHolder);

        if (rowAffected <= 0) {
            return null;
        }

        userProfile.setId(keyHolder.getKey().intValue());
        return userProfile;
    }

    @Override
    public boolean update(UserProfile userProfile) {
        final String sql = "UPDATE user_profile SET " +
                "first_name = ?, " +
                "last_name = ?, " +
                "gender = ?, " +
                "birthday = ?, " +
                "likes = ?, " +
                "dislikes = ?, " +
                "photo_url = ? " +
                "WHERE id = ?;";

        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userProfile.getFirstName());
            ps.setString(2, userProfile.getLastName());
            ps.setString(3, userProfile.getGender().toString());
            ps.setDate(4, userProfile.getBirthday() != null ? Date.valueOf(userProfile.getBirthday()) : null);

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                ps.setString(5, objectMapper.writeValueAsString(userProfile.getLikes()));
                ps.setString(6, objectMapper.writeValueAsString(userProfile.getDislikes()));
            } catch (Exception e) {
                throw new SQLException("Error serializing likes/dislikes to JSON", e);
            }

            ps.setString(7, userProfile.getPhotoUrl());
            ps.setInt(8, userProfile.getId());
            return ps;
        }) > 0;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM user_profile WHERE id = ?;", id) > 0;
    }
}
