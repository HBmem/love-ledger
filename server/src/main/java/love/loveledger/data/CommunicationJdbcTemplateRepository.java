package love.loveledger.data;

import love.loveledger.data.mappers.CommunicationMapper;
import love.loveledger.models.Communication;
import love.loveledger.models.CommunicationType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CommunicationJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    public CommunicationJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Communication> findAllCommunicationByUserId(int userId) {
        final String sql = "select communication_id, `date`, `type`, `description`, mood_score, relationship_id "
                + "from communication "
                + "where user_id = ?;";

        return jdbcTemplate.query(sql, new CommunicationMapper(), userId);
    }

    public Communication findCommunicationById(int communicationId) {
        final String sql = "select communication_id, `date`, `type`, `description`, mood_score, relationship_id "
                + "from communication "
                + "where communication_id = ?;";

        return jdbcTemplate.query(sql, new CommunicationMapper(), communicationId).stream()
                .findFirst()
                .orElse(null);
    }

    public Communication add(Communication communication) {
        final String sql = "insert into communication(`type`, `description`, `date`, mood_score, relationship_id) "
                + "values (?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, communication.getType().toString());
            ps.setString(2, communication.getDescription());
            ps.setDate(3, Date.valueOf(communication.getDate()));
            ps.setInt(4, communication.getMoodScore());
            ps.setInt(5, communication.getRelationshipId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        communication.setCommunicationId(keyHolder.getKey().intValue());
        return communication;
    }

    public boolean update(Communication communication) {
        final String sql = "update communication set "
                + "`type` = ?, "
                + "`description` = ?, "
                + "`date` = ?, "
                + "mood_score = ?, "
                + "relationship_id = ? "
                + "where communication_id = ?;";

        return jdbcTemplate.update(sql,
                communication.getType().toString(),
                communication.getDescription(),
                communication.getDate(),
                communication.getMoodScore(),
                communication.getRelationshipId(),
                communication.getCommunicationId()) > 0;
    }

    public boolean delete(int communication_id) {
        return jdbcTemplate.update("delete from communication where communication_id = ?;", communication_id) > 0;
    }
}
