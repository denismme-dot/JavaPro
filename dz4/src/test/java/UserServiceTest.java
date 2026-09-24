import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private AnnotationConfigApplicationContext ctx;
    private UserService userService;

    @BeforeEach
    void setUp() {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        userService = ctx.getBean(UserService.class);
        userService.getAll().forEach(userService::delete);
    }

    @AfterEach
    void tearDown() {
        if (ctx != null) {
            ctx.close();
        }
    }

    @Test
    void createAndGetById() {
        User created = userService.create("test_user");
        assertNotNull(created.getId());

        Optional<User> found = userService.getById(created.getId());
        assertTrue(found.isPresent());
        assertEquals("test_user", found.get().getUsername());
    }

    @Test
    void createWithBlankUsernameThrows() {
        assertThrows(IllegalArgumentException.class, () -> userService.create(""));
        assertThrows(IllegalArgumentException.class, () -> userService.create(null));
    }

    @Test
    void createDuplicateThrows() {
        userService.create("dup");
        assertThrows(IllegalStateException.class, () -> userService.create("dup"));
    }

    @Test
    void getAllReturnsAllUsers() {
        userService.create("u1");
        userService.create("u2");
        userService.create("u3");

        List<User> all = userService.getAll();
        assertEquals(3, all.size());
    }

    @Test
    void getByUsernameWorks() {
        userService.create("findme");
        Optional<User> found = userService.getByUsername("findme");
        assertTrue(found.isPresent());
        assertEquals("findme", found.get().getUsername());
    }

    @Test
    void deleteRemovesUser() {
        User created = userService.create("to_delete");
        assertTrue(userService.delete(created));
        assertTrue(userService.getById(created.getId()).isEmpty());
    }

    @Test
    void deleteByIdRemovesUser() {
        User created = userService.create("to_delete_by_id");
        assertTrue(userService.deleteById(created.getId()));
        assertTrue(userService.getById(created.getId()).isEmpty());
    }
}