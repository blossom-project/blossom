package fr.mgargadennec.blossom.autoconfigure.ui.web.administration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupService;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.web.administration.membership.MembershipsController;

@Configuration
@ConditionalOnClass(MembershipsController.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebAdministrationMembershipAutoConfiguration {

  @Bean
  public MembershipsController membershipsController(UserService userService,
      AssociationUserGroupService associationUserGroupService, GroupService groupService) {
    return new MembershipsController(userService, associationUserGroupService, groupService);
  }

}
