package fr.mgargadennec.blossom.autoconfigure.ui.web.system;

import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import fr.mgargadennec.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.system.sessions.SessionController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnBean(SessionRegistry.class)
@ConditionalOnClass(SessionController.class)
public class WebSystemSessionsAutoConfiguration {

  @Bean
  public MenuItem systemSessionMenuItem(MenuItemBuilder builder, @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder
      .key("sessions")
      .label("menu.system.sessions", true)
      .link("/blossom/system/sessions")
      .order(5)
      .icon("fa fa-plug")
      .parent(systemMenuItem)
      .privilege(sessionsPrivilegePlugin())
      .build();
  }

  @Bean
  public SessionController sessionController(SessionRegistry sessionRegistry) {
    return new SessionController(sessionRegistry);
  }

  @Bean
  public Privilege sessionsPrivilegePlugin() {
    return new SimplePrivilege("system","sessions", "manager");
  }
}
