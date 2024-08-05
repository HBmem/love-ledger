package love.loveledger.data;

import love.loveledger.data.mappers.MilestoneMapper;
import love.loveledger.models.Milestone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class MilestoneJdbcTemplateRepository implements MilestoneRepository {
    private final JdbcTemplate jdbcTemplate;

    public MilestoneJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Milestone> findAllMilestone() {
        final String sql = "select milestone_id, `name`, `description` "
                + "from milestone;";

        return jdbcTemplate.query(sql, new MilestoneMapper());
    }

    @Override
    public Milestone findMilestoneById(int milestoneId) {
        final String sql = "select milestone_id, `name`, `description` "
                + "from milestone "
                + "where milestone_id = ?;";

        return jdbcTemplate.query(sql, new MilestoneMapper(), milestoneId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Milestone add(Milestone milestone) {
        final String sql = "insert into milestone(`name`, `description`) "
                + "values (?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, milestone.getName());
            ps.setString(2, milestone.getDescription());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        milestone.setMilestoneId(keyHolder.getKey().intValue());
        return milestone;
    }

    @Override
    public boolean update(Milestone milestone) {
        final String sql = "update milestone set "
                + "`name` = ?, "
                + "`description` = ? "
                + "where milestone_id = ?;";
        return jdbcTemplate.update(sql,
                milestone.getName(),
                milestone.getDescription(),
                milestone.getMilestoneId()) > 0;
    }

    @Override
    public boolean delete(int milestone_id) {
        return jdbcTemplate.update("delete from milestone where milestone_id = ?;", milestone_id) > 0;
    }
}
