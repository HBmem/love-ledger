package com.hbmem.LoveLedger.data.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbmem.LoveLedger.model.Gender;
import com.hbmem.LoveLedger.model.LoveInterest;
import io.jsonwebtoken.lang.Collections;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LoveInterestMapper implements RowMapper<LoveInterest> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public LoveInterest mapRow(ResultSet resultSet, int i) throws SQLException {
        LoveInterest loveInterest = new LoveInterest();

        loveInterest.setId(resultSet.getInt("id"));
        loveInterest.setNickname(resultSet.getString("nickname"));
        loveInterest.setFirstName(resultSet.getString("first_name"));
        loveInterest.setLastName(resultSet.getString("last_name"));
        loveInterest.setGender(Gender.valueOf(resultSet.getString("gender")));
        loveInterest.setBirthday(resultSet.getDate("birthday").toLocalDate());

        try {
            List<String> likes = objectMapper.readValue(resultSet.getString("likes"), new TypeReference<List<String>>() {});
            List<String> dislikes = objectMapper.readValue(resultSet.getString("dislikes"), new TypeReference<List<String>>() {});

            loveInterest.setLikes(likes);
            loveInterest.setDislikes(dislikes);
        } catch (JsonProcessingException e) {
            loveInterest.setLikes(Collections.emptyList());
            loveInterest.setDislikes(Collections.emptyList());

            throw new RuntimeException(e);
        }

        loveInterest.setPhotoUrl(resultSet.getString("photo_url"));
        loveInterest.setUser_id(resultSet.getInt("user_id"));

        return loveInterest;
    }
}
