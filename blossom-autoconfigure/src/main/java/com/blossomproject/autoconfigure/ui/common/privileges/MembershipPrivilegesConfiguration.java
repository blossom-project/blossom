package com.blossomproject.autoconfigure.ui.common.privileges;

import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MembershipPrivilegesConfiguration {

  @Bean
  public Privilege membershipsReadPrivilegePlugin() {
    return new SimplePrivilege("administration", "memberships", "read");
  }

  @Bean
  public Privilege membershipsChangePrivilegePlugin() {
    return new SimplePrivilege("administration", "memberships", "change");
  }

}
