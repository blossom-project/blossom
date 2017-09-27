package fr.mgargadennec.blossom.autoconfigure.ui.web.administration;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupService;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.web.administration.membership.MembershipsController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MembershipsController.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebAdministrationMembershipAutoConfiguration {

  @Bean
  public MembershipsController membershipsController(
    AssociationUserGroupService associationUserGroupService,
    UserService userService, SearchEngineImpl<GroupDTO> userSearchEngine, GroupService groupService,
    SearchEngineImpl<GroupDTO> groupSearchEngine) {
    return new MembershipsController(associationUserGroupService, userService, userSearchEngine, groupService,groupSearchEngine);
  }

}
