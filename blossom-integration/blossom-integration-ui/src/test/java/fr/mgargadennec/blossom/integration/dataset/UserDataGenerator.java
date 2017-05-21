package fr.mgargadennec.blossom.integration.dataset;

import fr.mgargadennec.blossom.core.user.User;
import fr.mgargadennec.blossom.core.user.UserRepository;
import fr.mgargadennec.blossom.integration.builder.UserBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by zoula_000 on 19/05/2017.
 */
public class UserDataGenerator implements DataGenerator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void generateDatas() {

        User activeUser = new UserBuilder()
                .id(1L)
                .identifier("jdoe")
                .passwordHash(this.passwordEncoder.encode("demo"))
                .firstname("John")
                .lastname("Doe")
                .activated(true)
                .toUser();
        this.userRepository.save(activeUser);

        User inactiveUser = new UserBuilder()
                .id(2L)
                .identifier("cdoe")
                .passwordHash(this.passwordEncoder.encode("demo"))
                .firstname("Carl")
                .lastname("Doe")
                .activated(false)
                .toUser();
        this.userRepository.save(inactiveUser);
        
    }

}
