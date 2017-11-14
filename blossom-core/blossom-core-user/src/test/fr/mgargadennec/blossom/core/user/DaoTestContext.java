package fr.blossom.core.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by zoula_000 on 17/05/2017.
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EntityScan(basePackageClasses = User.class)
@Import({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class DaoTestContext {

    @Bean
    public UserDao userDao(UserRepository userRepository) {
        return new UserDaoImpl(userRepository);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            System.out.println("toto");
        };
    }

}
