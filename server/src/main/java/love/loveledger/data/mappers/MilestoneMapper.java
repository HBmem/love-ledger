package love.loveledger.data.mappers;

import love.loveledger.models.Milestone;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MilestoneMapper implements RowMapper<Milestone> {
    @Override
    public Milestone mapRow(ResultSet resultSet, int i) throws SQLException {
        Milestone milestone = new Milestone();

        milestone.setMilestoneId(resultSet.getInt("milestone_id"));
        milestone.setName(resultSet.getString("name"));
        milestone.setDescription(resultSet.getString("description"));

        return milestone;
    }
}
