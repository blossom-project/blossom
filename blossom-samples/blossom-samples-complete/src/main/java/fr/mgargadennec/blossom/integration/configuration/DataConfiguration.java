package fr.mgargadennec.blossom.integration.configuration;

import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupService;
import fr.mgargadennec.blossom.core.association_user_role.AssociationUserRoleService;
import fr.mgargadennec.blossom.core.common.PluginConstants;
import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.role.Role;
import fr.mgargadennec.blossom.core.role.RoleDTO;
import fr.mgargadennec.blossom.core.role.RoleDao;
import fr.mgargadennec.blossom.core.role.RoleService;
import fr.mgargadennec.blossom.core.user.User;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.fluttercode.datafactory.impl.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.plugin.core.PluginRegistry;

/**
 * Created by Maël Gargadennnec on 09/06/2017.
 */
@Configuration
public class DataConfiguration {

  private final static Logger LOGGER = LoggerFactory.getLogger(DataConfiguration.class);

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
      IntStream.range(0, 15).mapToObj(i -> {
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
        user.setLocale(Locale.FRENCH);
        user.setCivility(random.nextBoolean() ? User.Civility.MAN : User.Civility.WOMAN);
        user.setDescription(df.getRandomText(200, 600));
        return user;
      }).forEach(u -> service.create(u));
    };
  }

  @Bean
  public CommandLineRunner clrGroup(GroupService service, DataFactory df) {
    return args -> {
      IntStream.range(0, 10).mapToObj(i -> {
        GroupDTO group = new GroupDTO();
        group.setName("Name-" + i);
        group.setDescription(
          "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form Ipsum available."
            + i);
        return group;
      }).forEach(g -> service.create(g));
    };
  }

  @Bean
  public CommandLineRunner clrRole(RoleDao dao,
    @Qualifier(PluginConstants.PLUGIN_PRIVILEGES)
      PluginRegistry<Privilege, String> privilegesRegistry) {
    return args -> {
      IntStream.range(0, 10).mapToObj(i -> {
        Role role = new Role();
        role.setName("Name-" + i);
        role.setDescription(
          "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form Ipsum available."
            + i);
        role.setPrivileges(privilegesRegistry.getPlugins().stream().map(p -> p.privilege()).collect(
          Collectors.toList()));
        return role;
      }).forEach(role -> dao.create(role));
    };
  }


  @Bean
  public CommandLineRunner clrAssociationUserGroup(UserService userService,
    GroupService groupService, AssociationUserGroupService service) {
    return args -> {
      Page<UserDTO> someUsers = userService.getAll(new PageRequest(0, 50));
      Page<GroupDTO> groupDTOS = groupService.getAll(new PageRequest(0, 50));

      someUsers.forEach(user -> {
        groupDTOS.forEach(group -> {
          service.associate(user, group);
        });

        LOGGER
          .info("Association to groups for user {} are {}", user, service.getAllLeft(user).size());
      });
    };
  }

  @Bean
  public CommandLineRunner clrAssociationUserRole(UserService userService, RoleService roleService,
    AssociationUserRoleService service) {
    return args -> {
      Page<UserDTO> someUsers = userService.getAll(new PageRequest(0, 50));
      Page<RoleDTO> someRoles = roleService.getAll(new PageRequest(0, 50));

      someUsers.forEach(user -> {
        someRoles.forEach(role -> {
          service.associate(user, role);
          roleService.getOne(role.getId());

          LOGGER.info("Association users / roles {} are {} for user {} and {} for role {}", user,
            service.getAllLeft(user).size(), user.getId(), service.getAllRight(role), role.getId());
        });

        userService.getOne(user.getId());
        userService.getByEmail(user.getEmail());

      });
    };
  }
}
