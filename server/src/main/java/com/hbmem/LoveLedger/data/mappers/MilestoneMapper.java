package com.hbmem.LoveLedger.data.mappers;

import com.hbmem.LoveLedger.model.Milestone;
import com.hbmem.LoveLedger.model.MilestoneType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MilestoneMapper implements RowMapper<Milestone> {

    @Override
    public Milestone mapRow(ResultSet resultSet, int i) throws SQLException {
        Milestone milestone = new Milestone();

        milestone.setId(resultSet.getInt("id"));
        milestone.setName(resultSet.getString("name"));
        milestone.setDescription(resultSet.getString("description"));
        milestone.setType(MilestoneType.valueOf(resultSet.getString("type")));
        milestone.setCondition(resultSet.getString("condition"));

        return milestone;
    }
}
