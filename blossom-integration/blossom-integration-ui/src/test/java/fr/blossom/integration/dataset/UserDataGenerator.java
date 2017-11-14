package fr.blossom.sample.dataset;

import fr.blossom.core.user.User;
import fr.blossom.core.user.UserRepository;
import fr.blossom.sample.builder.UserBuilder;
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

        User johnDoe = new UserBuilder()
                .id(1L)
                .identifier("jdoe")
                .passwordHash(this.passwordEncoder.encode("demo"))
                .civility(User.Civility.MAN)
                .firstname("John")
                .lastname("Doe")
                .activated(true)
                .toUser();
        this.userRepository.save(johnDoe);

        User carlDoe = new UserBuilder()
                .id(2L)
                .identifier("cdoe")
                .passwordHash(this.passwordEncoder.encode("demo"))
                .civility(User.Civility.MAN)
                .firstname("Carl")
                .lastname("Doe")
                .activated(false)
                .toUser();
        this.userRepository.save(carlDoe);

        User lauraChapman = new UserBuilder()
                .id(3L)
                .identifier("lchapman")
                .civility(User.Civility.WOMAN)
                .passwordHash(this.passwordEncoder.encode("demo"))
                .firstname("Laura")
                .lastname("Chapman")
                .activated(true)
                .toUser();
        this.userRepository.save(lauraChapman);

    }

}
