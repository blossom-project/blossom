package com.blossom_project.autoconfigure.ui.common.privileges;

import com.blossom_project.core.common.utils.privilege.Privilege;
import com.blossom_project.core.common.utils.privilege.SimplePrivilege;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupPrivilegesConfiguration {

  @Bean
  public Privilege groupsReadPrivilegePlugin() {
    return new SimplePrivilege("administration", "groups", "read");
  }

  @Bean
  public Privilege groupsWritePrivilegePlugin() {
    return new SimplePrivilege("administration", "groups", "write");
  }

  @Bean
  public Privilege groupsCreatePrivilegePlugin() {
    return new SimplePrivilege("administration", "groups", "create");
  }

  @Bean
  public Privilege groupsDeletePrivilegePlugin() {
    return new SimplePrivilege("administration", "groups", "delete");
  }
}
