package love.loveledger.data.mappers;

import love.loveledger.models.Outing;
import love.loveledger.models.OutingType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OutingMapper implements RowMapper<Outing> {
    @Override
    public Outing mapRow(ResultSet resultSet, int i) throws SQLException {
        Outing outing = new Outing();

        outing.setOutingId(resultSet.getInt("outing_id"));
        outing.setName(resultSet.getString("name"));
        outing.setType(OutingType.valueOf(resultSet.getString("type")));
        outing.setDescription(resultSet.getString("description"));
        outing.setLocation(resultSet.getString("location"));
        outing.setOutcome(resultSet.getString("outcome"));
        outing.setCost(resultSet.getBigDecimal("cost"));
        outing.setRating(resultSet.getInt("rating"));
        outing.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
        outing.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
        outing.setRelationshipId(resultSet.getInt("relationship_id"));

        return outing;
    }
}
