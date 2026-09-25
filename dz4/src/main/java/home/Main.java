package home;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;

public class Main {

        public static void main(String[] args) {

                try (AnnotationConfigApplicationContext ctx =
                        new AnnotationConfigApplicationContext(AppConfig.class)) {

                        UserService userService = ctx.getBean(UserService.class);
                        userService.getAll().forEach(userService::delete);

                        System.out.println("\n=== CREATE ===");
                        User tanya = userService.create("tanya");
                        User den = userService.create("den");
                        User alex = userService.create("alex");
                        System.out.println(tanya);
                        System.out.println(den);
                        System.out.println(alex);

                        System.out.println("\n=== READ ALL ===");
                        List<User> all = userService.getAll();
                        all.forEach(System.out::println);
                        System.out.println("Total: " + all.size());

                        System.out.println("\n=== UPDATE ===");
                        tanya.setUsername("tanya_updated");
                        userService.update(tanya);
                        userService.getById(tanya.getId()).ifPresent(System.out::println);

                        System.out.println("\n=== DELETE ===");
                        userService.delete(den);
                        userService.deleteById(alex.getId());
                        userService.getAll().forEach(System.out::println);

                        System.out.println("\nDone.");
                }
        }
}
