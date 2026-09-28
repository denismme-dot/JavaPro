package home.service;

import home.config.DbConfig;
import home.dao.UserDao;
import home.dto.User;
import home.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private AnnotationConfigApplicationContext ctx;
    private UserService userService;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        ctx = new AnnotationConfigApplicationContext(DbConfig.class);
        DataSource dataSource = ctx.getBean(DataSource.class);
        userService = new UserService(new UserDao(dataSource));
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.execute("DELETE FROM java.users");
        jdbcTemplate.execute("SELECT java.restart_seq()");
    }

    @AfterEach
    void tearDown() {
        if (ctx != null) {
            ctx.close();
        }
    }

    @Test
    @DisplayName("create: сохраняет пользователя с валидным именем")
    void create_validUsername_returnsSavedUser() {
        User result = userService.create("tanya");

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("tanya", result.getUsername());
    }

    @Test
    @DisplayName("create: IllegalArgumentException при пустом имени")
    void create_blank_throws() {
        assertThrows(IllegalArgumentException.class, () -> userService.create(""));
        assertThrows(IllegalArgumentException.class, () -> userService.create(null));
    }

    @Test
    @DisplayName("create: IllegalStateException при дубликате")
    void create_duplicate_throws() {
        userService.create("tanya");
        assertThrows(IllegalStateException.class, () -> userService.create("tanya"));
    }

    @Test
    @DisplayName("getById: возвращает пользователя")
    void getById_returnsUser() {
        User created = userService.create("tanya");
        assertTrue(userService.getById(created.getId()).isPresent());
        assertEquals("tanya", userService.getById(created.getId()).orElseThrow().getUsername());
    }

    @Test
    @DisplayName("getById: пустой Optional для null")
    void getById_null_returnsEmpty() {
        assertTrue(userService.getById(null).isEmpty());
    }

    @Test
    @DisplayName("getAll: возвращает всех пользователей")
    void getAll_returnsAll() {
        userService.create("tanya");
        userService.create("den");

        List<User> all = userService.getAll();

        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("update: IllegalArgumentException при null id")
    void update_nullId_throws() {
        assertThrows(IllegalArgumentException.class, () -> userService.update(new User("no_id")));
        assertThrows(IllegalArgumentException.class, () -> userService.update(null));
    }

    @Test
    @DisplayName("delete: удаляет пользователя")
    void delete_removesUser() {
        User created = userService.create("tanya");

        assertTrue(userService.delete(created));
        assertTrue(userService.getById(created.getId()).isEmpty());
    }

    @Test
    @DisplayName("deleteById: false для null")
    void deleteById_null_returnsFalse() {
        assertFalse(userService.deleteById(null));
    }
}