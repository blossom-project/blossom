package com.blossom_project.autoconfigure.ui.web.administration;

import com.blossom_project.autoconfigure.ui.common.privileges.MembershipPrivilegesConfiguration;
import com.blossom_project.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import com.blossom_project.core.association_user_group.AssociationUserGroupService;
import com.blossom_project.core.group.GroupService;
import com.blossom_project.core.user.UserService;
import com.blossom_project.ui.web.administration.membership.MembershipsController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass({MembershipsController.class})
@ConditionalOnBean({AssociationUserGroupService.class, UserService.class, GroupService.class})
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Import(MembershipPrivilegesConfiguration.class)
public class WebAdministrationMembershipAutoConfiguration {

    @Bean
    public MembershipsController membershipsController(AssociationUserGroupService associationUserGroupService, UserService userService, GroupService groupService) {
        return new MembershipsController(associationUserGroupService, userService, groupService);
    }

}
