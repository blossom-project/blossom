package fr.blossom.autoconfigure.ui.web.module;

import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import fr.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.module.filemanager.FileDTO;
import fr.blossom.module.filemanager.FileService;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import fr.blossom.ui.web.content.filemanager.FileController;
import fr.blossom.ui.web.content.filemanager.FileManagerController;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */

@Configuration
@ConditionalOnBean(FileService.class)
@ConditionalOnProperty(prefix = "blossom.filemanager.", name = "enabled")
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebFileManagerAutoConfiguration {

  @Bean
  @Order(3)
  @ConditionalOnMissingBean(name = "contentMenuItem")
  public MenuItem contentMenuItem(MenuItemBuilder builder) {
    return builder
      .key("content")
      .label("menu.content", true)
      .icon("fa fa-book")
      .link("/blossom/content")
      .leaf(false)
      .build();
  }

  @Bean
  public MenuItem contentFileManagerMenuItem(MenuItemBuilder builder, @Qualifier("contentMenuItem") MenuItem contentMenuItem) {
    return builder
      .key("filemanager")
      .label("menu.content.filemanager", true)
      .link("/blossom/content/filemanager")
      .icon("fa fa-photo")
      .order(0)
      .privilege(fileManagerReadPrivilegePlugin())
      .parent(contentMenuItem).build();
  }

  @Bean
  public FileManagerController fileManagerController(FileService fileService, SearchEngineImpl<FileDTO> searchEngine) {
    return new FileManagerController(fileService, searchEngine);
  }

  @Bean
  public FileController fileController(FileService fileService) {
    return new FileController(fileService);
  }

  @Bean
  public Privilege fileManagerReadPrivilegePlugin() {
    return new SimplePrivilege("content","filemanager", "read");
  }

  @Bean
  public Privilege fileManagerWritePrivilegePlugin() {
    return new SimplePrivilege("content","filemanager", "write");
  }

  @Bean
  public Privilege fileManagerCreatePrivilegePlugin() {
    return new SimplePrivilege("content","filemanager", "create");
  }

  @Bean
  public Privilege fileManagerDeletePrivilegePlugin() {
    return new SimplePrivilege("content","filemanager", "delete");
  }
}
