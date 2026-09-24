import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@Import(DbConfig.class)
public class AppConfig {

    @Bean
    public UserDAO userDao(DataSource dataSource) {
        return new UserDAO(dataSource);
    }

    @Bean
    public UserService userService(UserDAO userDao) {
        return new UserService(userDao);
    }
}