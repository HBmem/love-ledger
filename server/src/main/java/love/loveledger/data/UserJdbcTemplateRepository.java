package love.loveledger.data;

import love.loveledger.data.mappers.UserMapper;
import love.loveledger.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public User findByUsername(String username) {
        final String sql = "select user_id, username, password_hash, disabled "
                + "from user "
                + "where username = ?;";

        User user = jdbcTemplate.query(sql, new UserMapper(), username).stream().findFirst().orElse(null);

        if (user != null) {
            user.setRoles(getRolesByUserId(user.getUserId()));
        }

        return user;
    }

    @Transactional
    public User add(User user) {
        final String sql = "insert into user (username, email, password_hash) values (?, ?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setUserId(keyHolder.getKey().intValue());

        updateRoles(user);

        return user;
    }

    @Transactional
    public void update(User user) {
        final String sql = "update `user` set "
                + "username = ?, "
                + "email = ?, "
                + "disabled = ? "
                + "where user_id = ?";

        jdbcTemplate.update(sql,
                user.getUsername(), user.getEmail(), user.isDisabled(), user.getUserId());
        updateRoles(user);
    }

    private void updateRoles(User user) {
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
