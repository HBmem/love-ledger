package love.loveledger.data;

import love.loveledger.data.mappers.LoveInterestMapper;
import love.loveledger.models.LoveInterest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class LoveInterestJdbcTemplateRepository implements LoveInterestRepository {
    private final JdbcTemplate jdbcTemplate;

    public LoveInterestJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<LoveInterest> findAllLoveInterestByUserId(int userId) {
        final String sql = "select love_interest_id, nickname, fname, lname, gender, birthday, hobbies, likes, dislikes, user_id "
                + "from love_interest "
                + "where user_id = ?;";

        return jdbcTemplate.query(sql, new LoveInterestMapper(), userId);
    }

    @Override
    public LoveInterest findLoveInterestById(int loveInterestId) {
        final String sql = "select love_interest_id, nickname, fname, lname, gender, birthday, hobbies, likes, dislikes, user_id "
                + "from love_interest "
                + "where love_interest_id = ?;";

        return jdbcTemplate.query(sql, new LoveInterestMapper(), loveInterestId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public LoveInterest add(LoveInterest loveInterest) {
        final String sql = "insert into love_interest(nickname, fname, lname, gender, birthday, hobbies, likes, dislikes, user_id) "
                + "values (?,?,?,?,?,?,?,?,?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, loveInterest.getNickname());
            ps.setString(2, loveInterest.getFirstName());
            ps.setString(3, loveInterest.getLastName());
            ps.setString(4, loveInterest.getGender().toString());
            ps.setDate(5, Date.valueOf(loveInterest.getBirthday()));
            ps.setString(6, loveInterest.getHobbiesString());
            ps.setString(7, loveInterest.getLikesString());
            ps.setString(8, loveInterest.getDislikesString());
            ps.setInt(9, loveInterest.getUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        loveInterest.setLoveInterestId(keyHolder.getKey().intValue());
        return loveInterest;
    }

    @Override
    public boolean update(LoveInterest loveInterest) {
        final String sql = "update love_interest set "
                + "nickname = ?, "
                + "fname = ?, "
                + "lname = ?, "
                + "gender = ?, "
                + "birthday = ?, "
                + "hobbies = ?, "
                + "likes = ?, "
                + "dislikes = ?, "
                + "user_id = ? "
                + "where love_interest_id = ?;";

        return jdbcTemplate.update(sql,
                loveInterest.getNickname(),
                loveInterest.getFirstName(),
                loveInterest.getLastName(),
                loveInterest.getGender().toString(),
                loveInterest.getBirthday(),
                loveInterest.getHobbiesString(),
                loveInterest.getLikesString(),
                loveInterest.getDislikesString(),
                loveInterest.getUserId(),
                loveInterest.getLoveInterestId()) > 0;
    }

    @Override
    public boolean delete(int loveInterestId) {
        return jdbcTemplate.update("delete from love_interest where love_interest_id = ?;", loveInterestId) > 0;
    }
}
