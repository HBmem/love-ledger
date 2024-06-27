package love.loveledger.data.mappers;

import love.loveledger.models.Gender;
import love.loveledger.models.LoveInterest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoveInterestMapper implements RowMapper<LoveInterest> {
    @Override
    public LoveInterest mapRow(ResultSet resultSet, int i) throws SQLException {
        LoveInterest loveInterest = new LoveInterest();

        loveInterest.setLoveInterestId(resultSet.getInt("love_interest_id"));
        loveInterest.setNickname(resultSet.getString("nickname"));
        loveInterest.setFirstName(resultSet.getString("fname"));
        loveInterest.setLastName(resultSet.getString("lname"));
        loveInterest.setGender(Gender.valueOf(resultSet.getString("gender")));
        loveInterest.setBirthday(resultSet.getDate("birthday").toLocalDate());
        loveInterest.setHobbies(resultSet.getString("hobbies"));
        loveInterest.setLikes(resultSet.getString("likes"));
        loveInterest.setDislikes(resultSet.getString("dislikes"));
        loveInterest.setUserId(resultSet.getInt("user_id"));

        return loveInterest;
    }
}
