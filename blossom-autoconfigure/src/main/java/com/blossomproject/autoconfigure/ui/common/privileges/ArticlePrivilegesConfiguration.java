package com.blossomproject.autoconfigure.ui.common.privileges;

import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArticlePrivilegesConfiguration {

  @Bean
  public Privilege articleReadPrivilegePlugin() {
    return new SimplePrivilege("content", "articles", "read");
  }

  @Bean
  public Privilege articleManagerWritePrivilegePlugin() {
    return new SimplePrivilege("content", "articles", "write");
  }

  @Bean
  public Privilege articleManagerCreatePrivilegePlugin() {
    return new SimplePrivilege("content", "articles", "create");
  }

  @Bean
  public Privilege articleManagerDeletePrivilegePlugin() {
    return new SimplePrivilege("content", "articles", "delete");
  }
}
