package love.loveledger.data;

import love.loveledger.data.mappers.ReminderMapper;
import love.loveledger.models.Reminder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class ReminderJdbcTemplateRepository implements ReminderRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReminderJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reminder> findAllReminderByUserId(int userId) {
        final String sql = "select reminder_id"
                + "from reminder "
                + "where user_id = ?;";

        return jdbcTemplate.query(sql, new ReminderMapper(), userId);
    }

    @Override
    public Reminder findReminderById(int reminderId) {
        final String sql = "select reminder_id"
                + "from reminder "
                + "where reminder_id = ?;";

        return jdbcTemplate.query(sql, new ReminderMapper(), reminderId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Reminder add(Reminder reminder) {
        final String sql = "insert into reminder(`name`, `date`, `description`, user_id, notable_day_id) "
                + "values (?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reminder.getName());
            ps.setDate(2, Date.valueOf(reminder.getDate()));
            ps.setString(3, reminder.getDescription());
            ps.setInt(4, reminder.getUserId());
            ps.setInt(5, reminder.getNotableDayId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        reminder.setReminderId(keyHolder.getKey().intValue());
        return reminder;
    }

    @Override
    public boolean update(Reminder reminder) {
        final String sql = "update reminder set "
                + "`name` = ?, "
                + "`date` = ?, "
                + "`description` = ?, "
                + "user_id = ?, "
                + "notable_day_id = ? "
                + "where reminder_id = ?;";

        return jdbcTemplate.update(sql,
                reminder.getName(),
                reminder.getDate(),
                reminder.getDate(),
                reminder.getDescription(),
                reminder.getUserId(),
                reminder.getNotableDayId(),
                reminder.getReminderId()) > 0;
    }

    @Override
    public boolean delete(int reminder_id) {
        return jdbcTemplate.update("delete from reminder where reminder_id = ?;", reminder_id) > 0;
    }
}
