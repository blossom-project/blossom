package fr.blossom.autoconfigure.ui.web.module;

import fr.blossom.autoconfigure.ui.common.privileges.FileManagerPrivilegesConfiguration;
import fr.blossom.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.module.filemanager.FileDTO;
import fr.blossom.module.filemanager.FileService;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import fr.blossom.ui.web.content.filemanager.FileController;
import fr.blossom.ui.web.content.filemanager.FileManagerController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */

@Configuration
@ConditionalOnProperty(prefix = "blossom.filemanager.", name = "enabled")
@ConditionalOnClass({FileService.class, FileController.class})
@ConditionalOnBean(FileService.class)
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Import(FileManagerPrivilegesConfiguration.class)
public class WebFileManagerAutoConfiguration {
  private final FileManagerPrivilegesConfiguration fileManagerPrivilegesConfiguration;

  public WebFileManagerAutoConfiguration(
    FileManagerPrivilegesConfiguration fileManagerPrivilegesConfiguration) {
    this.fileManagerPrivilegesConfiguration = fileManagerPrivilegesConfiguration;
  }


  @Bean
    @Order(3)
    @ConditionalOnMissingBean(name = "contentMenuItem")
    public MenuItem contentMenuItem(MenuItemBuilder builder) {
        return builder
                .key("content")
                .label("menu.content")
                .icon("fa fa-book")
                .link("/blossom/content")
                .leaf(false)
                .build();
    }

    @Bean
    public MenuItem contentFileManagerMenuItem(MenuItemBuilder builder, @Qualifier("contentMenuItem") MenuItem contentMenuItem) {
        return builder
                .key("filemanager")
                .label("menu.content.filemanager")
                .link("/blossom/content/filemanager")
                .icon("fa fa-photo")
                .order(0)
                .privilege(fileManagerPrivilegesConfiguration.fileManagerReadPrivilegePlugin())
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

}
