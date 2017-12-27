package fr.blossom.autoconfigure.ui.web.administration;

import fr.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.blossom.core.association_user_group.AssociationUserGroupService;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.core.group.GroupService;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.web.administration.membership.MembershipsController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MembershipsController.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebAdministrationMembershipAutoConfiguration {

    @Bean
    public MembershipsController membershipsController(AssociationUserGroupService associationUserGroupService, UserService userService, GroupService groupService) {
        return new MembershipsController(associationUserGroupService, userService, groupService);
    }

    @Bean
    public Privilege membershipsReadPrivilegePlugin() {
        return new SimplePrivilege("administration", "memberships", "read");
    }

    @Bean
    public Privilege membershipsChangePrivilegePlugin() {
        return new SimplePrivilege("administration", "memberships", "change");
    }

}
