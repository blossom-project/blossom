package fr.mgargadennec.blossom.autoconfigure.ui.web.system;

import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.system.build.BuildInfoController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(BuildInfoController.class)
public class WebSystemBuildAutoConfiguration {

  @Bean
  public MenuItem systemBuildMenuItem(MenuItemBuilder builder, @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder.key("build").label("menu.system.build", true).link("/blossom/system/build").order(0).icon("fa fa-archive").parent(systemMenuItem).build();
  }


  @Bean
  public BuildInfoController buildInfoController(BuildProperties buildProperties) {
    return new BuildInfoController(buildProperties);
  }

}
