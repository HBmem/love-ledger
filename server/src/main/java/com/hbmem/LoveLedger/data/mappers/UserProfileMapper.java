package com.hbmem.LoveLedger.data.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbmem.LoveLedger.model.Gender;
import com.hbmem.LoveLedger.model.UserProfile;
import io.jsonwebtoken.lang.Collections;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserProfileMapper implements RowMapper<UserProfile> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public UserProfile mapRow(ResultSet resultSet, int i) throws SQLException {
        UserProfile userProfile = new UserProfile();

        userProfile.setId(resultSet.getInt("id"));
        userProfile.setFirstName(resultSet.getString("first_name"));
        userProfile.setLastName(resultSet.getString("last_name"));
        userProfile.setGender(Gender.valueOf(resultSet.getString("gender")));
        userProfile.setBirthday(resultSet.getDate("birthday").toLocalDate());

        try {
            List<String> likes = objectMapper.readValue(resultSet.getString("likes"), new TypeReference<List<String>>() {});
            List<String> dislikes = objectMapper.readValue(resultSet.getString("dislikes"), new TypeReference<List<String>>() {});

            userProfile.setLikes(likes);
            userProfile.setDislikes(dislikes);
        } catch (JsonProcessingException e) {
            userProfile.setLikes(Collections.emptyList());
            userProfile.setDislikes(Collections.emptyList());

            throw new RuntimeException(e);
        }

        userProfile.setPhotoUrl(resultSet.getString("photo_url"));

        return userProfile;
    }
}
