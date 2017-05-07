package fr.mgargadennec.blossom.samples;

import fr.mgargadennec.blossom.autoconfigure.EnableBlossom;
import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupService;
import fr.mgargadennec.blossom.core.association_user_role.AssociationUserRoleService;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.role.RoleDTO;
import fr.mgargadennec.blossom.core.role.RoleService;
import fr.mgargadennec.blossom.core.user.User;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    public CommandLineRunner clr(UserService service) {
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
            }).forEach(u -> service.create(u));
        };
    }

    @Bean
    public CommandLineRunner clrGroup(GroupService service) {
        return args -> {
            IntStream.range(0, 1000).mapToObj(i -> {
                GroupDTO group = new GroupDTO();
                group.setId((long) i);
                group.setName("Name-" + i);
                group.setDescription("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form Ipsum available." + i);
                return group;
            }).forEach(g -> service.create(g));
        };
    }

    @Bean
    public CommandLineRunner clrRole(RoleService service) {
        return args -> {
            IntStream.range(0, 1000).mapToObj(i -> {
                RoleDTO role = new RoleDTO();
                role.setId((long) i);
                role.setName("Name-" + i);
                role.setDescription("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form Ipsum available." + i);
                return role;
            }).forEach(g -> service.create(g));
        };
    }


    @Bean
    public CommandLineRunner clrAssociationUserGroup(UserService userService, GroupService groupService, AssociationUserGroupService service) {
        return args -> {
            Page<UserDTO> someUsers = userService.getAll(new PageRequest(0, 50));
            Page<GroupDTO> groupDTOS = groupService.getAll(new PageRequest(0, 50));

            someUsers.forEach(user -> {
                groupDTOS.forEach(group -> {
                    service.associate(user, group);
                });

                LOGGER.info("Association to groups for user {} are {}", user, service.getAllLeft(user).size());
            });
        };
    }

    @Bean
    public CommandLineRunner clrAssociationUserRole(UserService userService, RoleService roleService, AssociationUserRoleService service) {
        return args -> {
            Page<UserDTO> someUsers = userService.getAll(new PageRequest(0, 50));
            Page<RoleDTO> someRoles = roleService.getAll(new PageRequest(0, 50));

            someUsers.forEach(user -> {
                someRoles.forEach(role -> {
                    service.associate(user, role);
                });

                LOGGER.info("Association to roles for user {} are {}", user, service.getAllLeft(user).size());
            });
        };
    }
}
