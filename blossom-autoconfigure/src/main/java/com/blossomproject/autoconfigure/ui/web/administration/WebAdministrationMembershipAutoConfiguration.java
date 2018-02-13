package com.blossomproject.autoconfigure.ui.web.administration;

import com.blossomproject.autoconfigure.ui.common.privileges.MembershipPrivilegesConfiguration;
import com.blossomproject.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import com.blossomproject.core.association_user_group.AssociationUserGroupService;
import com.blossomproject.core.group.GroupService;
import com.blossomproject.core.user.UserService;
import com.blossomproject.ui.web.administration.membership.MembershipsController;
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
