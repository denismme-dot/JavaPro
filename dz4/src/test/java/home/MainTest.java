package home;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainTest {

    @Test
    void dataSourceBeanShouldBeCreated() {
        try (AnnotationConfigApplicationContext ctx =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            DataSource dataSource = ctx.getBean(DataSource.class);
            assertNotNull(dataSource);
            System.out.println("DataSource: " + dataSource.getClass().getName());
        }
    }
}