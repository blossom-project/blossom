package com.blossom_project.ui.web.utils.session;

import com.google.common.collect.Sets;
import com.blossom_project.core.association_user_role.AssociationUserRoleDTO;
import com.blossom_project.core.association_user_role.AssociationUserRoleService;
import com.blossom_project.core.common.event.AfterDissociatedEvent;
import com.blossom_project.core.common.event.AssociatedEvent;
import com.blossom_project.core.common.event.UpdatedEvent;
import com.blossom_project.core.role.RoleDTO;
import com.blossom_project.ui.current_user.CurrentUser;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;

public class BlossomSessionRegistryImpl extends SessionRegistryImpl {

  private final AssociationUserRoleService associationUserRoleService;

  public BlossomSessionRegistryImpl(AssociationUserRoleService associationUserRoleService) {
    this.associationUserRoleService = associationUserRoleService;
  }

  @EventListener
  public void handleRolePrivilegeUpdate(UpdatedEvent<?> event) {
    if (event.getDTO() instanceof RoleDTO) {
      RoleDTO role = (RoleDTO) event.getDTO();
      List<AssociationUserRoleDTO> associations = associationUserRoleService.getAllRight(role);
      Set<Long> userIds = associations.stream().map(association -> association.getA().getId())
        .collect(Collectors.toSet());

      invalidateUserSessions(userIds);
    }
  }

  @EventListener
  public void handleRolePrivilegeUpdate(AssociatedEvent<?> event) {
    if (event.getDTO() instanceof AssociationUserRoleDTO) {
      AssociationUserRoleDTO association = (AssociationUserRoleDTO) event.getDTO();
      Long userId = association.getA().getId();

      invalidateUserSessions(Sets.newHashSet(userId));
    }
  }

  @EventListener
  public void handleRolePrivilegeUpdate(AfterDissociatedEvent<AssociationUserRoleDTO> event) {
    if (event.getDTO() instanceof AssociationUserRoleDTO) {
      AssociationUserRoleDTO association = (AssociationUserRoleDTO) event.getDTO();
      Long userId = association.getA().getId();

      invalidateUserSessions(Sets.newHashSet(userId));
    }
  }


  private void invalidateUserSessions(Set<Long> userIds) {

    this.getAllPrincipals().stream()
      .filter(u -> userIds.contains(((CurrentUser) u).getUser().getId()))
      .flatMap(u -> this.getAllSessions(u, false).stream())
      .forEach(SessionInformation::expireNow);
  }
}
