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
import org.fluttercode.datafactory.impl.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
  public DataFactory df() {
    return new DataFactory();
  }

  @Bean
  public Random random() {
    return new Random();
  }

  @Bean
  public CommandLineRunner clr(UserService service, DataFactory df, Random random) {
    return args -> {
      IntStream.range(0, 50).mapToObj(i -> {
        UserDTO user = new UserDTO();
        user.setIdentifier("Identifier-" + i);
        user.setPasswordHash("Password-" + i);
        user.setFirstname(df.getFirstName());
        user.setLastname(df.getLastName());
        user.setActivated(true);
        user.setEmail(df.getEmailAddress());
        user.setPhone("02.xx.xx.xx.xx");
        user.setFunction(df.getRandomWord());
        user.setCompany(df.getCity());
        user.setCivility(random.nextBoolean() ? User.Civility.MAN : User.Civility.WOMAN);
        user.setLastConnection(new Date(Instant.now().minus(random.nextInt(200000), ChronoUnit.SECONDS).toEpochMilli()));
        user.setDescription(df.getRandomText(200, 600));
        return user;
      }).forEach(u -> service.create(u));
    };
  }

  @Bean
  public CommandLineRunner clrGroup(GroupService service, DataFactory df) {
    return args -> {
      IntStream.range(0, 50).mapToObj(i -> {
        GroupDTO group = new GroupDTO();
        group.setName("Name-" + i);
        group.setDescription("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form Ipsum available." + i);
        return group;
      }).forEach(g -> service.create(g));
    };
  }

  @Bean
  public CommandLineRunner clrRole(RoleService service) {
    return args -> {
      IntStream.range(0, 15).mapToObj(i -> {
        RoleDTO role = new RoleDTO();
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
          roleService.getOne(role.getId());
        });

        userService.getOne(user.getId());
        userService.getByEmail(user.getEmail());

        LOGGER.info("Association to roles for user {} are {}", user, service.getAllLeft(user).size());
      });
    };
  }
}
