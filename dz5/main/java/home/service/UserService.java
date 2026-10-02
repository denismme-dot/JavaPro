package home.service;

import home.entity.User;
import home.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("User with username '" + username + "' already exists");
        }
        return userRepository.save(new User(username));
    }

    public Optional<User> getById(Long id) {
        if (id == null) return Optional.empty();
        return userRepository.findById(id);
    }

    public Optional<User> getByUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();
        return userRepository.findByUsername(username);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and its id must not be null");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalStateException("User with id " + user.getId() + " not found");
        }
        return userRepository.save(user);
    }

    public void delete(User user) {
        if (user == null || user.getId() == null) return;
        userRepository.deleteById(user.getId());
    }

    public void deleteById(Long id) {
        if (id == null) return;
        userRepository.deleteById(id);
    }

    public List<User> findUsersWithMinUsernameLength(int minLength) {
        return userRepository.findUsersWithMinUsernameLength(minLength);
    }
}