package home;

import home.entity.Product;
import home.entity.ProductType;
import home.entity.User;
import home.service.ProductService;
import home.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final ProductService productService;
    private final JdbcTemplate jdbcTemplate;

    public DataInitializer(UserService userService,
                           ProductService productService,
                           JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.productService = productService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== CommandLineRunner: выполнение операций ===");

        System.out.println("\n--- CLEANUP ---");
        productService.deleteAll();
        userService.deleteAll();
        jdbcTemplate.execute("SELECT java.restart_seq()");

        System.out.println("Users after cleanup: " + userService.getAll().size());
        System.out.println("Products after cleanup: " + productService.getAll().size());

        System.out.println("\n--- CREATE USERS ---");
        User tanya = userService.create("tanya");
        User den   = userService.create("den");
        User alex  = userService.create("alex");
        System.out.println(tanya);
        System.out.println(den);
        System.out.println(alex);

        System.out.println("\n--- CREATE PRODUCTS ---");
        Product tanyaAcc = productService.create(
                new Product("ACC-TANYA-1", new BigDecimal("1500.00"), ProductType.ACCOUNT, tanya));
        Product tanyaCard = productService.create(
                new Product("CRD-TANYA-1", new BigDecimal("250.75"), ProductType.CARD, tanya));
        Product denAcc = productService.create(
                new Product("ACC-DEN-1", new BigDecimal("3000.00"), ProductType.ACCOUNT, den));
        Product alexCard = productService.create(
                new Product("CRD-ALEX-1", new BigDecimal("100.00"), ProductType.CARD, alex));
        System.out.println(tanyaAcc);
        System.out.println(tanyaCard);
        System.out.println(denAcc);
        System.out.println(alexCard);

        System.out.println("\n--- PRODUCTS BY USER ID (tanya) ---");
        List<Product> tanyaProducts = productService.getByUserId(tanya.getId());
        tanyaProducts.forEach(System.out::println);
        System.out.println("Total for tanya: " + tanyaProducts.size());

        System.out.println("\n--- PRODUCT BY PRODUCT ID ---");
        productService.getById(tanyaAcc.getId())
                .ifPresent(p -> System.out.println("Found: " + p));

        System.out.println("\n--- PRODUCTS BY USER ID AND TYPE (tanya, CARD) ---");
        productService.getByUserIdAndType(tanya.getId(), ProductType.CARD)
                .forEach(System.out::println);

        System.out.println("\n--- ALL PRODUCTS ---");
        productService.getAll().forEach(System.out::println);

        System.out.println("\n=== CommandLineRunner: завершено ===");
    }
}