package fr.mgargadennec.blossom.autoconfigure.ui.web.administration;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import fr.mgargadennec.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.administration.group.GroupsController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(GroupsController.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebAdministrationGroupAutoConfiguration {

  @Bean
  public MenuItem administrationGroupMenuItem(MenuItemBuilder builder,
    @Qualifier("administrationMenuItem") MenuItem administrationMenuItem) {
    return builder
      .key("groups")
      .label("menu.administration.groups", true)
      .link("/blossom/administration/groups")
      .icon("fa fa-users")
      .order(2)
      .privilege(groupReadPrivilegePlugin())
      .parent(administrationMenuItem)
      .build();
  }

  @Bean
  public GroupsController groupsController(GroupService groupService,
    SearchEngineImpl<GroupDTO> searchEngine) {
    return new GroupsController(groupService, searchEngine);
  }

  @Bean
  public Privilege groupReadPrivilegePlugin() {
    return new SimplePrivilege("administration", "group", "read");
  }

  @Bean
  public Privilege groupWritePrivilegePlugin() {
    return new SimplePrivilege("administration", "group", "write");
  }

  @Bean
  public Privilege groupCreatePrivilegePlugin() {
    return new SimplePrivilege("administration", "group", "create");
  }

  @Bean
  public Privilege groupDeletePrivilegePlugin() {
    return new SimplePrivilege("administration", "group", "delete");
  }
}
