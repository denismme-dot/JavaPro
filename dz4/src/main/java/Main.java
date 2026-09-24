import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;
import java.util.Optional;

public class Main {
        private static Object deletedAlex;

        static void main() {
        try (AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class)) {

                UserService userService = ctx.getBean(UserService.class);
                System.out.println("\nGot bean: " + userService.getClass().getName());

                // ---------- Clear ALL ----------
                userService.getAll().forEach(userService::delete);
                System.out.println("\nTable cleared. Users count = " + userService.getAll().size());

                // ---------- CREATE ----------
                System.out.println("\n=== CREATE ===");
                User tanya = userService.create("tanya");
                User den = userService.create("den");
                User alex = userService.create("alex");
                System.out.println("Created: " + tanya);
                System.out.println("Created: " + den);
                System.out.println("Created: " + alex);

                // ---------- READ ONE (by id) ----------
                System.out.println("\n=== READ ONE (by id) ===");
                Optional<User> foundById = userService.getById(tanya.getId());
                foundById.ifPresent(u -> System.out.println("Found by id " + tanya.getId() + ": " + u));

                // ---------- READ ONE (by username) ----------
                System.out.println("\n=== READ ONE (by username) ===");
                Optional<User> foundByName = userService.getByUsername("den");
                foundByName.ifPresent(u -> System.out.println("Found by username 'den': " + u));

                // ---------- READ ALL ----------
                System.out.println("\n=== READ ALL ===");
                List<User> all = userService.getAll();
                all.forEach(System.out::println);
                System.out.println("Total: " + all.size());

                // ---------- UPDATE ----------
                System.out.println("\n=== UPDATE ===");
                tanya.setUsername("tanya new");
                boolean updated = userService.update(tanya);
                System.out.println("Update result: " + updated);
                userService.getById(tanya.getId())
                        .ifPresent(u -> System.out.println("After update: " + u));

                // ---------- DELETE (by object) ----------
                System.out.println("\n=== DELETE (by object) ===");
                boolean deletedDen = userService.delete(den);
                System.out.println("Deleted den: " + deletedDen);
                System.out.println("den exists now: " + userService.getByUsername("den").isPresent());

                // ---------- DELETE (by id) ----------
                System.out.println("\n=== DELETE (by id) ===");
                boolean deletedAlex = userService.deleteById(alex.getId());
                System.out.println("Deleted alex by id: " + deletedAlex);

                // ---------- FINAL STATE ----------
                System.out.println("\n=== FINAL STATE ===");
                userService.getAll().forEach(System.out::println);

                // ---------- ERROR CASES ----------
                System.out.println("\n=== ERROR CASES ===");

                try {
                    userService.create("");
                } catch (IllegalArgumentException e) {
                    System.out.println("Blank username -> IllegalArgumentException: " + e.getMessage());
                }

                try {
                    userService.create("tanya new");
                } catch (IllegalStateException e) {
                    System.out.println("Duplicate username -> IllegalStateException: " + e.getMessage());
                }

                System.out.println("\nDone.");
            }
    }
}
