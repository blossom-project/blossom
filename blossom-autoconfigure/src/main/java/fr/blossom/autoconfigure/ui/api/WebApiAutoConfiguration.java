package fr.blossom.autoconfigure.ui.api;

import fr.blossom.core.association_user_group.AssociationUserGroupService;
import fr.blossom.core.association_user_role.AssociationUserRoleService;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.group.GroupDTO;
import fr.blossom.core.group.GroupService;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleService;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.api.administration.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(UsersApiController.class)
public class WebApiAutoConfiguration {


    @Bean
    public UsersApiController usersApiController(UserService userService,
                                                 SearchEngineImpl<UserDTO> searchEngine) {
        return new UsersApiController(userService, searchEngine);
    }

    @Bean
    public GroupsApiController groupsApiController(GroupService groupService,
                                                   SearchEngineImpl<GroupDTO> searchEngine) {
        return new GroupsApiController(groupService, searchEngine);
    }

    @Bean
    public RolesApiController rolesApiController(RoleService roleService,
                                                 SearchEngineImpl<RoleDTO> searchEngine) {
        return new RolesApiController(roleService, searchEngine);
    }

    @Bean
    public MembershipsApiController membershipsApiController(
            AssociationUserGroupService associationUserGroupService, UserService userService,
            GroupService groupService) {
        return new MembershipsApiController(associationUserGroupService, userService, groupService);
    }

    @Bean
    public ResponsabilitiesApiController responsabilitiesApiController(
            AssociationUserRoleService associationUserRoleService, UserService userService,
            RoleService roleService) {
        return new ResponsabilitiesApiController(associationUserRoleService, userService, roleService);
    }

}
