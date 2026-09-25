package home;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("username"));

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User save(User user) {
        String sql = "INSERT INTO java.users (username) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getUsername());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            user.setId(key.longValue());
        }
        return user;
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT id, username FROM java.users WHERE id = ?";
        return jdbcTemplate.query(sql, USER_ROW_MAPPER, id).stream().findFirst();
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username FROM java.users WHERE username = ?";
        return jdbcTemplate.query(sql, USER_ROW_MAPPER, username).stream().findFirst();
    }

    public List<User> findAll() {
        String sql = "SELECT id, username FROM java.users ORDER BY id";
        return jdbcTemplate.query(sql, USER_ROW_MAPPER);
    }

    public boolean update(User user) {
        String sql = "UPDATE java.users SET username = ? WHERE id = ?";
        return jdbcTemplate.update(sql, user.getUsername(), user.getId()) > 0;
    }

    public boolean deleteById(Long id) {
        String sql = "DELETE FROM java.users WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}