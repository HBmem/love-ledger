package love.loveledger.data;

import love.loveledger.data.mappers.UserMapper;
import love.loveledger.models.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserJdbcTemplateRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public AppUser findByUserId(int userId) {
        final String sql = "select user_id, username, password_hash, email, profile_image_url, is_disabled, is_verified, fname, lname, gender, birthday "
                + "from `user` "
                + "where user_id = ?;";

        AppUser user = jdbcTemplate.query(sql, new UserMapper(), userId).stream().findFirst().orElse(null);

        if (user != null) {
            user.setRoles(getRolesByUserId(user.getUserId()));
        }

        return user;
    }

    @Override
    @Transactional
    public AppUser findByUsername(String username) {
        final String sql = "select user_id, username, password_hash, email, profile_image_url, is_disabled, is_verified, fname, lname, gender, birthday "
                + "from `user` "
                + "where username = ?;";

        AppUser user = jdbcTemplate.query(sql, new UserMapper(), username).stream().findFirst().orElse(null);

        if (user != null) {
            user.setRoles(getRolesByUserId(user.getUserId()));
        }

        return user;
    }

    @Override
    @Transactional
    public AppUser add(AppUser user) {
        final String sql = "insert into `user` (username, password_hash, email, profile_image_url, is_disabled, is_verified, fname, lname, gender, birthday) "
        + "values (?,?,?,?,?,?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getProfileImageURL());
            ps.setBoolean(5, user.isDisabled());
            ps.setBoolean(6, user.isVerified());
            ps.setString(7, user.getFirstName());
            ps.setString(8, user.getLastName());
            ps.setString(9, user.getGender().toString());
            if (user.getBirthday() == null) {
                ps.setDate(10, null);
            } else {
                ps.setDate(10, Date.valueOf(user.getBirthday()));
            }
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setUserId(keyHolder.getKey().intValue());

        updateRoles(user);

        return user;
    }

    @Override
    @Transactional
    public void update(AppUser user) {
        final String sql = "update `user` set "
                + "username = ?, "
                + "password_hash = ?, "
                + "email = ?, "
                + "profile_image_url = ?, "
                + "is_disabled = ?, "
                + "is_verified = ?, "
                + "fname = ?, "
                + "lname = ?, "
                + "gender = ?, "
                + "birthday = ? "
                + "where user_id = ?;";

        jdbcTemplate.update(sql,
                user.getUsername(), user.getEmail(), user.isDisabled(), user.getUserId());
        updateRoles(user);
    }

    private void updateRoles(AppUser user) {
        jdbcTemplate.update("delete from user_role where user_id = ?;", user.getUserId());

        if (user.getRoles() == null) {
            return;
        }

        for (String roleName : user.getRoles()) {
            String sql = "insert into user_role (user_id, role_id) "
                    + "select ?, role_id from role where `name` = ?;";

            jdbcTemplate.update(sql, user.getUserId(), roleName);
        }
    }

    private List<String> getRolesByUserId(int userId) {
        final String sql = "select r.name "
                + "from user_role ur "
                + "inner join role r on ur.role_id = r.role_id "
                + "where ur.user_id = ?";

        return jdbcTemplate.query(sql, (resultSet, rowId) -> resultSet.getString("name"), userId);
    }
}
