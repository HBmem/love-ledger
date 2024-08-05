package love.loveledger.data;

import love.loveledger.data.mappers.NotableDayMapper;
import love.loveledger.models.NotableDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class NotableDayJdbcTemplateRepository implements NotableDayRepository {
    private final JdbcTemplate jdbcTemplate;

    public NotableDayJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<NotableDay> findAll() {
        final String sql = "select notable_day_id, `name`, `description`, `day`, `month` "
                + "from notable_day;";

        return jdbcTemplate.query(sql, new NotableDayMapper());
    }

    @Override
    public NotableDay findById(int notableDayId) {
        final String sql = "select notable_day_id, `name`, `description`, `day`, `month` "
                + "from notable_day"
                + "where notable_day_id = ?";

        return jdbcTemplate.query(sql, new NotableDayMapper(), notableDayId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public NotableDay add(NotableDay notableDay) {
        final String sql = "insert into notable_day(`name`, `description`, `day`, `month`) "
                + "values (?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, notableDay.getName());
            ps.setString(2, notableDay.getDescription());
            ps.setInt(3, notableDay.getDay());
            ps.setInt(4, notableDay.getMonth());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        notableDay.setNotableDayId(keyHolder.getKey().intValue());
        return notableDay;
    }

    @Override
    public boolean update(NotableDay notableDay) {
        final String sql = "update notable_day set "
                + "`name` = ?, "
                + "`description` = ?,"
                + "`day` = ?, "
                + "`month` = ? "
                + "where notable_day_id = ?;";

        return jdbcTemplate.update(sql,
                notableDay.getName(),
                notableDay.getDescription(),
                notableDay.getDay(),
                notableDay.getMonth(),
                notableDay.getNotableDayId()) > 0;
    }

    @Override
    public boolean delete(int notableDayId) {
        return jdbcTemplate.update("delete from notable_day where notable_day_id = ?;", notableDayId) > 0;
    }
}
