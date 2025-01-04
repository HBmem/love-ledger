package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.data.mappers.RelationshipMapper;
import com.hbmem.LoveLedger.model.Relationship;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class RelationshipJdbcTemplateRepository implements RelationshipRepository {
    private final JdbcTemplate jdbcTemplate;

    public RelationshipJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Relationship findByRelationshipById(int id) {
        final String sql = "SELECT id, start_date, end_date, relationship_status, importance_level, is_official, user_id, love_interest_id " +
                "FROM relationship " +
                "WHERE id = ?;";

        return jdbcTemplate.query(sql, new RelationshipMapper(), id).stream()
                .findFirst()
                .orElse(null);
    }
    @Override
    public List<Relationship> findAllRelationshipByUserId(int userId) {
        final String sql = "SELECT id, start_date, end_date, relationship_status, importance_level, is_official, user_id, love_interest_id " +
                "FROM relationship " +
                "WHERE user_id = ?;";

        return jdbcTemplate.query(sql, new RelationshipMapper(), userId);
    }

    @Override
    public List<Relationship> findAllRelationshipByLoveInterestId(int loveInterestId) {
        final String sql = "SELECT id, start_date, end_date, relationship_status, importance_level, is_official, user_id, love_interest_id " +
                "FROM relationship " +
                "WHERE love_interest_id = ?;";

        return jdbcTemplate.query(sql, new RelationshipMapper(), loveInterestId);
    }

    @Override
    public Relationship add(Relationship relationship) {
        final String sql = "INSERT INTO relationship(start_date, end_date, relationship_status, importance_level, is_official, user_id, love_interest_id) " +
                "VALUES(?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, relationship.getStartDate() != null ? Date.valueOf(relationship.getStartDate()) : null);
            ps.setDate(2, relationship.getEndDate() != null ? Date.valueOf(relationship.getEndDate()) : null);
            ps.setString(3, relationship.getRelationshipStatus().toString());
            ps.setInt(4, relationship.getImportanceLevel());
            ps.setBoolean(5, relationship.isOfficial());
            ps.setInt(6, relationship.getUserId());
            ps.setInt(7, relationship.getLoveInterestId());

            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        relationship.setId(keyHolder.getKey().intValue());
        return relationship;
    }

    @Override
    public boolean update(Relationship relationship) {
        final String sql = "UPDATE relationship SET " +
                "start_date = ?, " +
                "end_date = ?, " +
                "relationship_status = ?, " +
                "importance_level = ?, " +
                "is_official = ?, " +
                "user_id = ?, " +
                "love_interest_id = ? " +
                "WHERE id = ?;";

        return jdbcTemplate.update(sql,
                relationship.getStartDate() != null ? relationship.getStartDate() : null,
                relationship.getEndDate() != null ? relationship.getEndDate() : null,
                relationship.getRelationshipStatus().toString(),
                relationship.getImportanceLevel(),
                relationship.isOfficial(),
                relationship.getUserId(),
                relationship.getLoveInterestId(),
                relationship.getId()) > 0;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE from relationship WHERE id = ?;", id) > 0;
    }
}
