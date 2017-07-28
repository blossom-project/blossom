package fr.mgargadennec.blossom.ui.web.administration.responsability;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.mgargadennec.blossom.core.association_user_role.AssociationUserRoleService;
import fr.mgargadennec.blossom.core.role.RoleDTO;
import fr.mgargadennec.blossom.core.role.RoleService;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;

@BlossomController("/administration/responsability")
public class ResponsabilityController {

  private final UserService userService;
  private final AssociationUserRoleService associationUserRoleService;
  private final RoleService roleService;

  public ResponsabilityController(UserService userService, AssociationUserRoleService associationUserRoleService,
      RoleService roleService) {
    this.userService = userService;
    this.associationUserRoleService = associationUserRoleService;
    this.roleService = roleService;
  }

  @PostMapping
  @ResponseBody
  public void handleResponsabilityToRoleCreation(Long userId, Long roleId, Model model) {
    UserDTO user = this.userService.getOne(userId);
    RoleDTO role = this.roleService.getOne(roleId);

    this.associationUserRoleService.associate(user, role);
  }

  @DeleteMapping
  @ResponseBody
  public void handleMembershipToRoleDeletion(Long userId, Long roleId, Model model) {
    UserDTO user = this.userService.getOne(userId);
    RoleDTO role = this.roleService.getOne(roleId);

    this.associationUserRoleService.dissociate(user, role);

  }
}
