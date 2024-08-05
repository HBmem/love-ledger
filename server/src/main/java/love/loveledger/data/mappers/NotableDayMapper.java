package love.loveledger.data.mappers;

import love.loveledger.models.NotableDay;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotableDayMapper implements RowMapper<NotableDay> {

    @Override
    public NotableDay mapRow(ResultSet resultSet, int i) throws SQLException {
        NotableDay notableDay = new NotableDay();

        notableDay.setNotableDayId(resultSet.getInt("notable_day_id"));
        notableDay.setName(resultSet.getString("name"));
        notableDay.setDescription(resultSet.getString("description"));
        notableDay.setDay(resultSet.getInt("day"));
        notableDay.setMonth(resultSet.getInt("month"));

        return notableDay;
    }
}
