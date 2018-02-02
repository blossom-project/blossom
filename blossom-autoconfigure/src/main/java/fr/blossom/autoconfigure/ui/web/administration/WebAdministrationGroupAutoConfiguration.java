package fr.blossom.autoconfigure.ui.web.administration;

import fr.blossom.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.core.group.GroupDTO;
import fr.blossom.core.group.GroupService;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import fr.blossom.ui.web.administration.group.GroupsController;
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
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
public class WebAdministrationGroupAutoConfiguration {

    @Bean
    public MenuItem administrationGroupMenuItem(MenuItemBuilder builder,
                                                @Qualifier("administrationMenuItem") MenuItem administrationMenuItem) {
        return builder
                .key("groups")
                .label("menu.administration.groups")
                .link("/blossom/administration/groups")
                .icon("fa fa-users")
                .order(2)
                .privilege(groupsReadPrivilegePlugin())
                .parent(administrationMenuItem)
                .build();
    }

    @Bean
    public GroupsController groupsController(GroupService groupService,
                                             SearchEngineImpl<GroupDTO> searchEngine) {
        return new GroupsController(groupService, searchEngine);
    }

    @Bean
    public Privilege groupsReadPrivilegePlugin() {
        return new SimplePrivilege("administration", "groups", "read");
    }

    @Bean
    public Privilege groupsWritePrivilegePlugin() {
        return new SimplePrivilege("administration", "groups", "write");
    }

    @Bean
    public Privilege groupsCreatePrivilegePlugin() {
        return new SimplePrivilege("administration", "groups", "create");
    }

    @Bean
    public Privilege groupsDeletePrivilegePlugin() {
        return new SimplePrivilege("administration", "groups", "delete");
    }
}
