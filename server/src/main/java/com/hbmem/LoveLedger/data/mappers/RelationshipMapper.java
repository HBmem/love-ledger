package com.hbmem.LoveLedger.data.mappers;

import com.hbmem.LoveLedger.model.Relationship;
import com.hbmem.LoveLedger.model.RelationshipStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RelationshipMapper implements RowMapper<Relationship> {
    @Override
    public Relationship mapRow(ResultSet resultSet, int i) throws SQLException {
        Relationship relationship = new Relationship();

        relationship.setId(resultSet.getInt("id"));
        relationship.setStartDate(resultSet.getDate("start_date") != null ? resultSet.getDate("start_date").toLocalDate() : null);
        relationship.setEndDate(resultSet.getDate("end_date") != null ? resultSet.getDate("end_date").toLocalDate() : null);
        relationship.setRelationshipStatus(RelationshipStatus.valueOf(resultSet.getString("relationship_status")));
        relationship.setImportanceLevel(resultSet.getInt("importance_level"));
        relationship.setOfficial(resultSet.getBoolean("is_official"));
        relationship.setUserId(resultSet.getInt("user_id"));
        relationship.setLoveInterestId(resultSet.getInt("love_interest_id"));

        return relationship;
    }
}
