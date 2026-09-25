package home;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {

    private AnnotationConfigApplicationContext ctx;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        userDao = ctx.getBean(UserDao.class);
        userDao.findAll().forEach(u -> userDao.deleteById(u.getId()));
    }

    @AfterEach
    void tearDown() {
        if (ctx != null) {
            ctx.close();
        }
    }

    @Test
    void saveAndFindById() {
        User saved = userDao.save(new User("test_user"));
        assertNotNull(saved.getId());

        Optional<User> found = userDao.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("test_user", found.get().getUsername());
    }

    @Test
    void findAllReturnsAllSavedUsers() {
        userDao.save(new User("u1"));
        userDao.save(new User("u2"));
        userDao.save(new User("u3"));

        List<User> all = userDao.findAll();
        assertEquals(3, all.size());
    }

    @Test
    void updateChangesUsername() {
        User saved = userDao.save(new User("old_name"));
        saved.setUsername("new_name");

        assertTrue(userDao.update(saved));
        assertEquals("new_name", userDao.findById(saved.getId()).orElseThrow().getUsername());
    }

    @Test
    void deleteRemovesUser() {
        User saved = userDao.save(new User("to_delete"));
        assertTrue(userDao.deleteById(saved.getId()));
        assertTrue(userDao.findById(saved.getId()).isEmpty());
    }
}