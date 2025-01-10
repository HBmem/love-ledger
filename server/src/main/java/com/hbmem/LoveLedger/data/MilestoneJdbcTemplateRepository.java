package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.data.mappers.MilestoneMapper;
import com.hbmem.LoveLedger.model.Milestone;
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
    public Milestone findById(int id) {
        final String sql = "SELECT id, `name`, `description`, `type`, `condition` " +
                "FROM milestone " +
                "WHERE id = ?;";

        return jdbcTemplate.query(sql, new MilestoneMapper(), id).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Milestone> findAllMilestones() {
        final String sql = "select id, `name`, `description`, `type`, `condition` " +
                "from milestone;";

        return jdbcTemplate.query(sql, new MilestoneMapper());
    }

    @Override
    public Milestone add(Milestone milestone) {
        final String sql = "INSERT INTO  milestone(`name`, `description`, `type`, `condition`) " +
                "VALUES(?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, milestone.getName());
            ps.setString(2, milestone.getDescription());
            ps.setString(3, milestone.getType().toString());
            ps.setString(4, milestone.getCondition());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        milestone.setId(keyHolder.getKey().intValue());
        return milestone;
    }

    @Override
    public boolean update(Milestone milestone) {
        final String sql = "UPDATE milestone SET " +
                "`name` = ?, " +
                "`description` = ?, " +
                "`type` = ?, " +
                "`condition` = ? " +
                "WHERE id = ?;";
        return jdbcTemplate.update(sql,
                milestone.getName(),
                milestone.getDescription(),
                milestone.getType().toString(),
                milestone.getCondition(),
                milestone.getId()) > 0;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM milestone where id = ?;", id) > 0;
    }
}
