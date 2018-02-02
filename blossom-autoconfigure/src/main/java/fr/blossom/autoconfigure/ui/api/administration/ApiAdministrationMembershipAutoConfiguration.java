package fr.blossom.autoconfigure.ui.api.administration;

import fr.blossom.autoconfigure.ui.api.ApiInterfaceAutoConfiguration;
import fr.blossom.autoconfigure.ui.common.privileges.MembershipPrivilegesConfiguration;
import fr.blossom.core.association_user_group.AssociationUserGroupService;
import fr.blossom.core.group.GroupService;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.api.administration.MembershipsApiController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass({MembershipsApiController.class})
@ConditionalOnBean({AssociationUserGroupService.class, UserService.class, GroupService.class})
@AutoConfigureAfter(ApiInterfaceAutoConfiguration.class)
@Import(MembershipPrivilegesConfiguration.class)
public class ApiAdministrationMembershipAutoConfiguration {

  @Bean
  public MembershipsApiController membershipsApiController(
    AssociationUserGroupService associationUserGroupService, UserService userService,
    GroupService groupService) {
    return new MembershipsApiController(associationUserGroupService, userService, groupService);
  }

}
