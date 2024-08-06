package love.loveledger.data.mappers;

import love.loveledger.models.Reminder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReminderMapper implements RowMapper<Reminder> {
    @Override
    public Reminder mapRow(ResultSet resultSet, int i) throws SQLException {
        Reminder reminder = new Reminder();

        reminder.setReminderId(resultSet.getInt("reminder_id"));
        reminder.setName(resultSet.getString("name"));
        reminder.setDescription(resultSet.getString("description"));
        reminder.setDate(resultSet.getDate("date").toLocalDate());
        reminder.setUserId(resultSet.getInt("user_id"));
        reminder.setRelationshipId(resultSet.getInt("relationship_id"));
        reminder.setNotableDayId(resultSet.getInt("notable_day_id"));

        return reminder;
    }
}
