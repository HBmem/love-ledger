package love.loveledger.data.mappers;

import love.loveledger.models.Communication;
import love.loveledger.models.CommunicationType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommunicationMapper implements RowMapper<Communication> {

    @Override
    public Communication mapRow(ResultSet resultSet, int i) throws SQLException {
        Communication communication = new Communication();

        communication.setCommunicationId(resultSet.getInt("communication_id"));
        communication.setType(CommunicationType.valueOf(resultSet.getString("type")));
        communication.setDescription(resultSet.getString("description"));
        communication.setDate(resultSet.getDate("date").toLocalDate());
        communication.setMoodScore(resultSet.getInt("mood_score"));
        communication.setRelationshipId(resultSet.getInt("relationship_id"));

        return  communication;
    }
}
