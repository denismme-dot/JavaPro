package home;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User create(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (userDao.findByUsername(username).isPresent()) {
            throw new IllegalStateException("User with username '" + username + "' already exists");
        }
        return userDao.save(new User(username));
    }

    public Optional<User> getById(Long id) {
        if (id == null) return Optional.empty();
        return userDao.findById(id);
    }

    public Optional<User> getByUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();
        return userDao.findByUsername(username);
    }

    public List<User> getAll() {
        return userDao.findAll();
    }

    public boolean update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and its id must not be null");
        }
        return userDao.update(user);
    }

    public boolean delete(User user) {
        if (user == null || user.getId() == null) return false;
        return userDao.deleteById(user.getId());
    }

    public boolean deleteById(Long id) {
        if (id == null) return false;
        return userDao.deleteById(id);
    }
}