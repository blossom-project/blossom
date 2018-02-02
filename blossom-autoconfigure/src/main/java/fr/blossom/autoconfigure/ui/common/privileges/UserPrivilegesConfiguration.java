package fr.blossom.autoconfigure.ui.common.privileges;

import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserPrivilegesConfiguration {

  @Bean
  public Privilege usersReadPrivilege() {
    return new SimplePrivilege("administration", "users", "read");
  }

  @Bean
  public Privilege usersWritePrivilege() {
    return new SimplePrivilege("administration", "users", "write");
  }

  @Bean
  public Privilege usersCreatePrivilege() {
    return new SimplePrivilege("administration", "users", "create");
  }

  @Bean
  public Privilege usersDeletePrivilege() {
    return new SimplePrivilege("administration", "users", "delete");
  }

}
