package home.dao;

import home.config.DbConfig;
import home.dao.UserDao;
import home.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    private AnnotationConfigApplicationContext ctx;
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() throws RuntimeException {
        ctx = new AnnotationConfigApplicationContext(DbConfig.class);
        DataSource dataSource = ctx.getBean(DataSource.class);
        userDao = new UserDao(dataSource);

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
    @DisplayName("save: присваивает id и сохраняет пользователя")
    void save_assignsId() {
        User saved = userDao.save(new User("tanya"));

        assertNotNull(saved.getId());
        assertEquals("tanya", saved.getUsername());
    }

    @Test
    @DisplayName("save: id увеличивается для каждого нового пользователя")
    void save_incrementsId() {
        User tanya = userDao.save(new User("tanya"));
        User den = userDao.save(new User("den"));
        User alex = userDao.save(new User("alex"));

        assertEquals(1L, tanya.getId());
        assertEquals(2L, den.getId());
        assertEquals(3L, alex.getId());
    }

    @Test
    @DisplayName("findById: возвращает пользователя")
    void findById_returnsUser() {
        User saved = userDao.save(new User("tanya"));

        Optional<User> found = userDao.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("tanya", found.get().getUsername());
    }

    @Test
    @DisplayName("findById: пустой Optional для несуществующего id")
    void findById_missing_returnsEmpty() {
        assertTrue(userDao.findById(999L).isEmpty());
    }

    @Test
    @DisplayName("findByUsername: возвращает пользователя")
    void findByUsername_returnsUser() {
        userDao.save(new User("tanya"));

        Optional<User> found = userDao.findByUsername("tanya");

        assertTrue(found.isPresent());
        assertEquals("tanya", found.get().getUsername());
    }

    @Test
    @DisplayName("findByUsername: пустой Optional для несуществующего имени")
    void findByUsername_missing_returnsEmpty() {
        assertTrue(userDao.findByUsername("ghost").isEmpty());
    }

    @Test
    @DisplayName("findAll: возвращает всех пользователей, отсортированных по id")
    void findAll_returnsAllSortedById() {
        userDao.save(new User("tanya"));
        userDao.save(new User("den"));
        userDao.save(new User("alex"));

        List<User> all = userDao.findAll();

        assertEquals(3, all.size());
        assertEquals("tanya", all.get(0).getUsername());
        assertEquals("den", all.get(1).getUsername());
        assertEquals("alex", all.get(2).getUsername());
    }

    @Test
    @DisplayName("findAll: пустой список для пустой таблицы")
    void findAll_empty_returnsEmptyList() {
        assertTrue(userDao.findAll().isEmpty());
    }

    @Test
    @DisplayName("update: изменяет username")
    void update_changesUsername() {
        User saved = userDao.save(new User("tanya"));
        saved.setUsername("tanya_updated");

        assertTrue(userDao.update(saved));
        assertEquals("tanya_updated",
                userDao.findById(saved.getId()).orElseThrow().getUsername());
    }

    @Test
    @DisplayName("update: false для несуществующего id")
    void update_missing_returnsFalse() {
        assertFalse(userDao.update(new User(999L, "ghost")));
    }

    @Test
    @DisplayName("deleteById: удаляет пользователя")
    void deleteById_removesUser() {
        User saved = userDao.save(new User("den"));

        assertTrue(userDao.deleteById(saved.getId()));
        assertTrue(userDao.findById(saved.getId()).isEmpty());
    }

    @Test
    @DisplayName("deleteById: false для несуществующего id")
    void deleteById_missing_returnsFalse() {
        assertFalse(userDao.deleteById(999L));
    }
}