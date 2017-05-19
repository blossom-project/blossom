package fr.mgargadennec.blossom.autoconfigure.ui.web.module;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.module.filemanager.FileService;
import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.content.filemanager.FileManagerController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@Configuration
@ConditionalOnBean(FileService.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebFileManagerAutoConfiguration {


  @Bean
  @Order(3)
  @ConditionalOnMissingBean(name = "contentMenuItem")
  public MenuItem contentMenuItem(MenuItemBuilder builder) {
    return builder.key("content").label("menu.content", true).icon("fa fa-book").link("/blossom/content").build();
  }

  @Bean
  public MenuItem contentFileManagerMenuItem(MenuItemBuilder builder, @Qualifier("contentMenuItem") MenuItem contentMenuItem) {
    return builder.key("filemanager").label("menu.content.filemanager", true).link("/blossom/content/filemanager").icon("fa fa-photo").order(0).parent(contentMenuItem).build();
  }

  @Bean
  public FileManagerController fileManagerController(FileService fileService) {
    return new FileManagerController(fileService);
  }
}
