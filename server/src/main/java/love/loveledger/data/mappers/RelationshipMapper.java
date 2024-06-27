package love.loveledger.data.mappers;

import love.loveledger.models.Relationship;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RelationshipMapper implements RowMapper<Relationship> {

    @Override
    public Relationship mapRow(ResultSet resultSet, int i) throws SQLException {
        Relationship relationship = new Relationship();
        relationship.setRelationshipId(resultSet.getInt("relationship_id"));
        relationship.setStartDate(resultSet.getDate("start_date").toLocalDate());
        if (resultSet.getDate("end_date") != null) {
            relationship.setEndDate(resultSet.getDate("end_date").toLocalDate());
        }
        relationship.setOfficial(resultSet.getBoolean("is_official"));
        relationship.setLabels(resultSet.getString("labels"));
        relationship.setUserId(resultSet.getInt("user_id"));
        relationship.setLoveInterestId(resultSet.getInt("love_interest_id"));

        return relationship;
    }
}
