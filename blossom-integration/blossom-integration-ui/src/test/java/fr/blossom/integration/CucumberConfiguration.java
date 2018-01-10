package fr.blossom.sample;

import fr.blossom.core.user.UserRepository;
import fr.blossom.sample.dataset.DataGenerator;
import fr.blossom.sample.dataset.UserDataGenerator;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * Created by zoula_000 on 19/05/2017.
 */
@Configuration
@EnableAutoConfiguration
public class CucumberConfiguration {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
        final List<DataGenerator> datasGenerators = Lists.newArrayList(
                new UserDataGenerator(userRepository, passwordEncoder)
        );
        return args -> datasGenerators.forEach(DataGenerator::generateDatas);
    }

}
