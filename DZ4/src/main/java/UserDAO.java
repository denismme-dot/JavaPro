import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("username"));

    public UserDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User save(User user) {
        String sql = "INSERT INTO java.users (username) VALUES (?) RETURNING id";

        Long id = jdbcTemplate.queryForObject(sql, Long.class, user.getUsername());
        user.setId(id);
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