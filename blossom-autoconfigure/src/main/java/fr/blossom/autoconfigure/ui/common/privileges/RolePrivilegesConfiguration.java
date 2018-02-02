package fr.blossom.autoconfigure.ui.common.privileges;

import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RolePrivilegesConfiguration {


  @Bean
  public Privilege rolesReadPrivilegePlugin() {
    return new SimplePrivilege("administration", "roles", "read");
  }

  @Bean
  public Privilege rolesWritePrivilegePlugin() {
    return new SimplePrivilege("administration", "roles", "write");
  }

  @Bean
  public Privilege rolesCreatePrivilegePlugin() {
    return new SimplePrivilege("administration", "roles", "create");
  }

  @Bean
  public Privilege rolesDeletePrivilegePlugin() {
    return new SimplePrivilege("administration", "roles", "delete");
  }

}
