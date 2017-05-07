package fr.mgargadennec.blossom.samples;

import fr.mgargadennec.blossom.autoconfigure.EnableBlossom;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.user.User;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@EnableBlossom
@SpringBootApplication
public class SampleUIOverride {
    private final static Logger LOGGER = LoggerFactory.getLogger(SampleUIOverride.class);

    public static void main(String[] args) {
        SpringApplication.run(SampleUIOverride.class, args);
    }

    @Bean
    public MainController mainController(UserService userService) {
        return new MainController(userService);
    }

    @Bean
    public CommandLineRunner clr(UserService userService) {
        return args -> {
            IntStream.range(0, 1000).mapToObj(i -> {
                UserDTO user = new UserDTO();
                user.setId((long) i);
                user.setIdentifier("Identifier-" + i);
                user.setPasswordHash("Password-" + i);
                user.setFirstname("Firstname-" + i);
                user.setLastname("Lastname-" + i);
                user.setActivated(true);
                user.setEmail("Email-" + i);
                user.setPhone("Phone-" + i);
                user.setFunction("Function-" + i);
                user.setCompany("Function-" + i);
                user.setCivility(User.Civility.MALE);
                user.setLastConnection(new Date(System.currentTimeMillis() - new Random().nextInt(500000)));
                user.setDescription("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form Ipsum available." + i);
                return user;
            }).forEach(u -> userService.create(u));
        };
    }

    @Bean
    public CommandLineRunner clrGroup(GroupService groupService) {
        return args -> {
            IntStream.range(0, 1000).mapToObj(i -> {
                GroupDTO group = new GroupDTO();
                group.setId((long) i);
                group.setName("Name-" + i);
                group.setDescription("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form Ipsum available." + i);
                return group;
            }).forEach(g -> groupService.create(g));
        };
    }
}
