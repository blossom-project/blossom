package com.blossom_project.autoconfigure.ui.common.privileges;

import com.blossom_project.core.common.utils.privilege.Privilege;
import com.blossom_project.core.common.utils.privilege.SimplePrivilege;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileManagerPrivilegesConfiguration {

  @Bean
  public Privilege fileManagerReadPrivilegePlugin() {
    return new SimplePrivilege("content", "filemanager", "read");
  }

  @Bean
  public Privilege fileManagerWritePrivilegePlugin() {
    return new SimplePrivilege("content", "filemanager", "write");
  }

  @Bean
  public Privilege fileManagerCreatePrivilegePlugin() {
    return new SimplePrivilege("content", "filemanager", "create");
  }

  @Bean
  public Privilege fileManagerDeletePrivilegePlugin() {
    return new SimplePrivilege("content", "filemanager", "delete");
  }
}
