package love.loveledger.data;

import love.loveledger.data.mappers.OutingMapper;
import love.loveledger.models.Outing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class OutingJdbcTemplateRepository implements OutingRepository {
    private final JdbcTemplate jdbcTemplate;


    public OutingJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Outing> findAllOutingsByRelationshipId(int relationshipId) {
        final String sql = "select outing_id, `name`, `type`, `description`, location, outcome, cost, rating, start_time, end_time, relationship_id "
                + "from outing "
                + "where relationship_id = ?;";

        return jdbcTemplate.query(sql, new OutingMapper(), relationshipId);
    }

    @Override
    public Outing findOutingById(int outingId) {
        final String sql = "select outing_id, `name`, `type`, `description`, location, outcome, cost, rating, start_time, end_time, relationship_id "
                + "from outing "
                + "where outing_id = ?;";

        return jdbcTemplate.query(sql, new OutingMapper(), outingId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Outing add(Outing outing) {
        final String sql = "insert into outing(`name`, `type`, `description`, location, cost, rating, outcome, start_time, end_time, relationship_id) "
                + "values (?,?,?,?,?,?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, outing.getName());
            ps.setString(2, outing.getType().toString());
            ps.setString(3, outing.getDescription());
            ps.setString(4, outing.getLocation());
            ps.setBigDecimal(5, outing.getCost());
            ps.setInt(6, outing.getRating());
            ps.setString(7, outing.getOutcome());
            ps.setTimestamp(8, Timestamp.valueOf(outing.getStartTime()));
            ps.setTimestamp(9, Timestamp.valueOf(outing.getEndTime()));
            ps.setInt(10, outing.getRelationshipId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        outing.setOutingId(keyHolder.getKey().intValue());
        return outing;
    }

    @Override
    public boolean update(Outing outing) {
        final String sql = "update outing set "
                + "`name` = ?, "
                + "`type` = ?, "
                + "`description` = ?, "
                + "location = ?, "
                + "outcome = ?, "
                + "cost = ?, "
                + "rating = ?, "
                + "start_time = ?, "
                + "end_time = ?, "
                + "relationship_id = ? "
                + "where outing_id = ?;";

        return jdbcTemplate.update(sql,
                outing.getName(),
                outing.getType().toString(),
                outing.getDescription(),
                outing.getLocation(),
                outing.getOutcome(),
                outing.getCost(),
                outing.getRating(),
                outing.getStartTime(),
                outing.getEndTime(),
                outing.getRelationshipId(),
                outing.getOutingId()) > 0;
    }

    @Override
    public boolean delete(int outingId) {
        return jdbcTemplate.update("delete from outing where outing_id = ?;", outingId) > 0;
    }
}
