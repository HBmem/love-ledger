package com.hbmem.LoveLedger.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbmem.LoveLedger.data.mappers.LoveInterestMapper;
import com.hbmem.LoveLedger.model.LoveInterest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class LoveInterestJdbcTemplateRepository implements LoveInterestRepository {
    private final JdbcTemplate jdbcTemplate;

    public LoveInterestJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public LoveInterest findLoveInterestById(int id) {
        final String sql = "SELECT id, nickname, first_name, last_name, gender, birthday, likes, dislikes, photo_url, user_id " +
                "FROM love_interest " +
                "WHERE id = ?;";

        return jdbcTemplate.query(sql, new LoveInterestMapper(), id).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<LoveInterest> findAllLoveInterestByUserId(int userId) {
        final String sql = "SELECT id, nickname, first_name, last_name, gender, birthday, likes, dislikes, photo_url, user_id " +
                "FROM love_interest " +
                "WHERE user_id = ?;";

        return jdbcTemplate.query(sql, new LoveInterestMapper(), userId);
    }

    @Override
    public LoveInterest add(LoveInterest loveInterest) throws JsonProcessingException {
        final String sql = "INSERT INTO love_interest(nickname, first_name, last_name, gender, birthday, likes, dislikes, photo_url, user_id) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, loveInterest.getNickname());
            ps.setString(2, loveInterest.getFirstName());
            ps.setString(3, loveInterest.getLastName());
            ps.setString(4, loveInterest.getGender().toString());
            ps.setDate(5, loveInterest.getBirthday() != null ? Date.valueOf(loveInterest.getBirthday()) : null);

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                ps.setString(6, objectMapper.writeValueAsString(loveInterest.getLikes()));
                ps.setString(7, objectMapper.writeValueAsString(loveInterest.getDislikes()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            ps.setString(8, loveInterest.getPhotoUrl());
            ps.setInt(9, loveInterest.getUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        loveInterest.setId(keyHolder.getKey().intValue());
        return loveInterest;
    }

    @Override
    public boolean update(LoveInterest loveInterest) {
        final String sql = "UPDATE love_interest SET " +
                "nickname = ?, " +
                "first_name = ?, " +
                "last_name = ?, " +
                "gender = ?, " +
                "birthday = ?, " +
                "likes = ?, " +
                "dislikes = ?, " +
                "photo_url = ?, " +
                "user_id = ? " +
                "WHERE id = ?;";

        return jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, loveInterest.getNickname());
            ps.setString(2, loveInterest.getFirstName());
            ps.setString(3, loveInterest.getLastName());
            ps.setString(4, loveInterest.getGender().toString());
            ps.setDate(5, loveInterest.getBirthday() != null ? Date.valueOf(loveInterest.getBirthday()) : null);

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                ps.setString(6, objectMapper.writeValueAsString(loveInterest.getLikes()));
                ps.setString(7, objectMapper.writeValueAsString(loveInterest.getDislikes()));
            } catch (Exception e) {
                throw new SQLException("Error serializing likes/dislikes to JSON", e);
            }

            ps.setString(8, loveInterest.getPhotoUrl());
            ps.setInt(9, loveInterest.getUserId());
            ps.setInt(10, loveInterest.getId());
            return ps;
        }) > 0;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM love_interest WHERE id = ?;", id) > 0;
    }
}
