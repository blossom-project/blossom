package com.blossomproject.autoconfigure.ui.common.privileges;

import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResponsabilityPrivilegesConfiguration {

  @Bean
  public Privilege responsabilitiesReadPrivilegePlugin() {
    return new SimplePrivilege("administration", "responsabilities", "read");
  }

  @Bean
  public Privilege responsabilitiesChangePrivilegePlugin() {
    return new SimplePrivilege("administration", "responsabilities", "change");
  }

}
