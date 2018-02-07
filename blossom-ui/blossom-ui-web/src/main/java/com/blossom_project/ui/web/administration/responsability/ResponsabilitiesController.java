package com.blossom_project.ui.web.administration.responsability;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.blossom_project.core.association_user_role.AssociationUserRoleDTO;
import com.blossom_project.core.association_user_role.AssociationUserRoleService;
import com.blossom_project.core.association_user_role.UpdateAssociationUserRoleForm;
import com.blossom_project.core.role.RoleDTO;
import com.blossom_project.core.role.RoleService;
import com.blossom_project.core.user.UserDTO;
import com.blossom_project.core.user.UserService;
import com.blossom_project.ui.stereotype.BlossomController;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@BlossomController
@RequestMapping("/administration/responsabilities")
public class ResponsabilitiesController {

  private final UserService userService;
  private final AssociationUserRoleService associationUserRoleService;
  private final RoleService roleService;

  public ResponsabilitiesController(UserService userService, AssociationUserRoleService associationUserRoleService,
      RoleService roleService) {
    this.userService = userService;
    this.associationUserRoleService = associationUserRoleService;
    this.roleService = roleService;
  }

  @GetMapping("/users/{id}/roles")
  @PreAuthorize("hasAuthority('administration:responsabilities:read')")
  public ModelAndView getUserMembershipsPage(@PathVariable Long id, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    List<AssociationUserRoleDTO> associations = associationUserRoleService.getAllLeft(user);

    return this.responsabilitiesView(new PageImpl<AssociationUserRoleDTO>(associations),"user");
  }

  @GetMapping("/users/{id}/roles/_edit")
  @PreAuthorize("hasAuthority('administration:responsabilities:change')")
  public ModelAndView getUserMembershipsForm(@PathVariable Long id, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    return this
      .responsabilitiesForm(associationUserRoleService.getAllLeft(user), user, null, "user", model);
  }

  @Transactional
  @PostMapping("/users/{id}/roles/_edit")
  @PreAuthorize("hasAuthority('administration:responsabilities:change')")
  public ModelAndView handleUserMembershipsForm(@PathVariable Long id, Model model,
    @ModelAttribute("form") UpdateAssociationUserRoleForm updateAssociationUserRoleForm) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }

    Set<Long> roleIds =  updateAssociationUserRoleForm.getIds();
    List<AssociationUserRoleDTO> associations = associationUserRoleService.getAllLeft(user);
    Set<Long> actualRoleIds = associations.stream().map(association -> association.getB().getId())
      .collect(
        Collectors.toSet());

    Set<Long> toDelete = Sets.difference(actualRoleIds, roleIds);
    List<RoleDTO> toDeleteRoles = roleService.getAll(Lists.newArrayList(toDelete));
    for (RoleDTO toDeleteRole : toDeleteRoles) {
      associationUserRoleService.dissociate(user, toDeleteRole);
    }

    Set<Long> toCreate = Sets.difference(roleIds, actualRoleIds);
    List<RoleDTO> toCreateRoles = roleService.getAll(Lists.newArrayList(toCreate));
    for (RoleDTO toCreateRole : toCreateRoles) {
      associationUserRoleService.associate(user, toCreateRole);
    }

    associations = associationUserRoleService.getAllLeft(user);
    return this.responsabilitiesView(new PageImpl<>(associations), "user");
  }

  @GetMapping("/roles/{id}/users")
  @PreAuthorize("hasAuthority('administration:responsabilities:read')")
  public ModelAndView getRoleMembershipsPage(@PathVariable Long id, Model model) {
    RoleDTO role = this.roleService.getOne(id);
    if (role == null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    List<AssociationUserRoleDTO> associations = associationUserRoleService.getAllRight(role);

    return this.responsabilitiesView(new PageImpl<>(associations), "role");
  }

  @GetMapping("/roles/{id}/users/_edit")
  @PreAuthorize("hasAuthority('administration:responsabilities:change')")
  public ModelAndView getRoleMembershipsForm(@PathVariable Long id, Model model) {
    RoleDTO role = this.roleService.getOne(id);
    if (role== null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    return this
      .responsabilitiesForm(associationUserRoleService.getAllRight(role), null, role, "role", model);
  }

  @Transactional
  @PostMapping("/roles/{id}/users/_edit")
  @PreAuthorize("hasAuthority('administration:responsabilities:change')")
  public ModelAndView handleRoleResponsabilitiesForm(@PathVariable Long id, Model model,
    @ModelAttribute("form") UpdateAssociationUserRoleForm updateAssociationUserRoleForm) {
    RoleDTO role = this.roleService.getOne(id);
    if (role== null) {
      throw new NoSuchElementException(String.format("Role=%s not found", id));
    }
    Set<Long> userIds = updateAssociationUserRoleForm.getIds();
    List<AssociationUserRoleDTO> associations = associationUserRoleService.getAllRight(role);
    Set<Long> actualUserIds = associations.stream().map(association -> association.getA().getId())
      .collect(
        Collectors.toSet());

    Set<Long> toDelete = Sets.difference(actualUserIds , userIds);
    List<UserDTO> toDeleteUsers = userService.getAll(Lists.newArrayList(toDelete));
    for (UserDTO toDeleteUser: toDeleteUsers) {
      associationUserRoleService.dissociate(toDeleteUser, role);
    }

    Set<Long> toCreate = Sets.difference(userIds, actualUserIds );
    List<UserDTO> toCreateUsers = userService.getAll(Lists.newArrayList(toCreate));
    for (UserDTO toCreateUser : toCreateUsers) {
      associationUserRoleService.associate(toCreateUser, role);
    }

    associations = associationUserRoleService.getAllRight(role);
    return this.responsabilitiesView(new PageImpl<>(associations), "role");
  }

  private ModelAndView responsabilitiesView(Page<AssociationUserRoleDTO> responsabilities, String mode) {
    return new ModelAndView("blossom/responsabilities/responsabilities-" + mode,"responsabilities", responsabilities);
  }

  private ModelAndView responsabilitiesForm(List<AssociationUserRoleDTO> memberships, UserDTO user,
    RoleDTO role, String mode, Model model) {
    model.addAttribute("associations", memberships);
    model.addAttribute("users", userService.getAll());
    model.addAttribute("roles", roleService.getAll());
    model.addAttribute("user", user);
    model.addAttribute("role", role);
    return new ModelAndView("blossom/responsabilities/responsabilities-" + mode + "-edit", model.asMap());
  }
}
