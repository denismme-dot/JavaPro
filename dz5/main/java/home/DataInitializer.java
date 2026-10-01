package home;

import home.entity.User;
import home.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== CommandLineRunner ===");

        System.out.println("\n--- CLEANUP ---");
        userService.getAll().forEach(userService::delete);
        System.out.println("Users after cleanup: " + userService.getAll().size());

        System.out.println("\n--- CREATE ---");
        User tanya = userService.create("tanya");
        User den = userService.create("den");
        User alex = userService.create("alex");
        System.out.println(tanya);
        System.out.println(den);
        System.out.println(alex);

        System.out.println("\n--- READ ALL ---");
        List<User> all = userService.getAll();
        all.forEach(System.out::println);
        System.out.println("Total: " + all.size());

        System.out.println("\n--- READ ONE ---");
        userService.getById(tanya.getId()).ifPresent(u -> System.out.println("Found: " + u));
        userService.getByUsername("den").ifPresent(u -> System.out.println("Found: " + u));

        System.out.println("\n--- UPDATE ---");
        tanya.setUsername("tanya_updated");
        userService.update(tanya);
        System.out.println("Updated: " + userService.getById(tanya.getId()).orElseThrow());

        System.out.println("\n--- CUSTOM QUERY ---");
        List<User> longNames = userService.findUsersWithMinUsernameLength(4);
        longNames.forEach(System.out::println);

        System.out.println("\n--- DELETE ---");
        userService.delete(den);
        userService.deleteById(alex.getId());
        System.out.println("Remaining users:");
        userService.getAll().forEach(System.out::println);

        System.out.println("\n=== CommandLineRunner: end ===");
    }
}