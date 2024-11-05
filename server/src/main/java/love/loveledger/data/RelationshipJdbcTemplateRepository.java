package love.loveledger.data;

import love.loveledger.data.mappers.RelationshipMapper;
import love.loveledger.models.Relationship;
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
    public List<Relationship> findAllRelationshipByUserId(int userId) {
        final String sql = "select relationship_id, start_date, end_date, relationship_status, importance_level, is_official, user_id, love_interest_id "
                + "from relationship "
                + "where user_id = ?;";

        return jdbcTemplate.query(sql, new RelationshipMapper(), userId);
    }

    @Override
    public Relationship findRelationshipById(int relationshipId) {
        final String sql = "select relationship_id, start_date, end_date, relationship_status, importance_level, is_official, user_id, love_interest_id "
                + "from relationship "
                + "where relationship_id = ?;";

        return jdbcTemplate.query(sql, new RelationshipMapper(), relationshipId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Relationship add(Relationship relationship) {
        final String sql = "insert into relationship (start_date, end_date, relationship_status, importance_level, is_official, user_id, love_interest_id) "
        + "values (?,?,?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(relationship.getStartDate()));
            ps.setDate(2, relationship.getEndDate() == null ? null : Date.valueOf(relationship.getEndDate()));
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

        relationship.setRelationshipId(keyHolder.getKey().intValue());
        return relationship;
    }

    @Override
    public boolean update(Relationship relationship) {
        final String sql = "update relationship set "
                + "start_date = ?, "
                + "end_date = ?, "
                + "relationship_status = ?, "
                + "importance_level = ?, "
                + "is_official = ?, "
                + "user_id = ?, "
                + "love_interest_id = ? "
                + "where relationship_id = ?;";

        return jdbcTemplate.update(sql,
                relationship.getStartDate(), relationship.getEndDate(), relationship.getRelationshipStatus().toString(),
                relationship.getImportanceLevel(), relationship.isOfficial(), relationship.getUserId(),
                relationship.getLoveInterestId(), relationship.getRelationshipId()) > 0;
    }

    @Override
    public boolean delete(int relationshipId) {
        return jdbcTemplate.update("delete from relationship where relationship_id = ?;", relationshipId) > 0;
    }
}
