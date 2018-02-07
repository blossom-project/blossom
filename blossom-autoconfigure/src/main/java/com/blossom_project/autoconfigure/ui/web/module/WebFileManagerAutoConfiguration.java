package com.blossom_project.autoconfigure.ui.web.module;

import com.blossom_project.autoconfigure.ui.common.privileges.FileManagerPrivilegesConfiguration;
import com.blossom_project.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import com.blossom_project.core.common.search.SearchEngineImpl;
import com.blossom_project.module.filemanager.FileDTO;
import com.blossom_project.module.filemanager.FileService;
import com.blossom_project.ui.menu.MenuItem;
import com.blossom_project.ui.menu.MenuItemBuilder;
import com.blossom_project.ui.web.content.filemanager.FileController;
import com.blossom_project.ui.web.content.filemanager.FileManagerController;
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
