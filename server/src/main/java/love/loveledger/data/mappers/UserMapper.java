package love.loveledger.data.mappers;

import love.loveledger.models.Gender;
import love.loveledger.models.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<AppUser> {
    @Override
    public AppUser mapRow(ResultSet resultSet, int i) throws SQLException {
        AppUser user = new AppUser();
        user.setUserId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password_hash"));
        user.setEmail(resultSet.getString("email"));
        user.setProfileImageURL(resultSet.getString("profile_image_url"));
        user.setDisabled(resultSet.getBoolean("is_disabled"));
        user.setVerified(resultSet.getBoolean("is_verified"));
        user.setFirstName(resultSet.getString("fname"));
        user.setLastName(resultSet.getString("lname"));
        user.setGender(Gender.valueOf(resultSet.getString("gender")));
        if (resultSet.getDate("birthday") != null) {
            user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        }

        return user;
    }
}
