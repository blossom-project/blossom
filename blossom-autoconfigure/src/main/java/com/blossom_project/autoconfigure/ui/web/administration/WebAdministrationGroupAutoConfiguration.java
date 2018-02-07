package com.blossom_project.autoconfigure.ui.web.administration;

import com.blossom_project.autoconfigure.ui.common.privileges.GroupPrivilegesConfiguration;
import com.blossom_project.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import com.blossom_project.core.common.search.SearchEngineImpl;
import com.blossom_project.core.group.GroupDTO;
import com.blossom_project.core.group.GroupService;
import com.blossom_project.ui.menu.MenuItem;
import com.blossom_project.ui.menu.MenuItemBuilder;
import com.blossom_project.ui.web.administration.group.GroupsController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass({GroupService.class, GroupsController.class})
@ConditionalOnBean(GroupService.class)
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Import(GroupPrivilegesConfiguration.class)
public class WebAdministrationGroupAutoConfiguration {

  private final GroupPrivilegesConfiguration groupPrivilegesConfiguration;

  public WebAdministrationGroupAutoConfiguration(
    GroupPrivilegesConfiguration groupPrivilegesConfiguration) {
    this.groupPrivilegesConfiguration = groupPrivilegesConfiguration;
  }

  @Bean
    public MenuItem administrationGroupMenuItem(MenuItemBuilder builder,
                                                @Qualifier("administrationMenuItem") MenuItem administrationMenuItem) {
        return builder
                .key("groups")
                .label("menu.administration.groups")
                .link("/blossom/administration/groups")
                .icon("fa fa-users")
                .order(2)
                .privilege(groupPrivilegesConfiguration.groupsReadPrivilegePlugin())
                .parent(administrationMenuItem)
                .build();
    }

    @Bean
    public GroupsController groupsController(GroupService groupService,
                                             SearchEngineImpl<GroupDTO> searchEngine) {
        return new GroupsController(groupService, searchEngine);
    }
}
