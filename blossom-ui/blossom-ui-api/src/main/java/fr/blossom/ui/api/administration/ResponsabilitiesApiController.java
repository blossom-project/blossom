package fr.blossom.ui.api.administration;

import com.google.common.base.Preconditions;
import fr.blossom.core.association_user_role.AssociationUserRoleDTO;
import fr.blossom.core.association_user_role.AssociationUserRoleService;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleService;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.stereotype.BlossomApiController;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomApiController
@RequestMapping("/administration/responsabilities")
public class ResponsabilitiesApiController {

  private final AssociationUserRoleService associationUserRoleService;
  private final UserService userService;
  private final RoleService roleService;

  public ResponsabilitiesApiController(AssociationUserRoleService associationUserRoleService,
    UserService userService, RoleService roleService) {
    this.associationUserRoleService = associationUserRoleService;
    this.userService = userService;
    this.roleService = roleService;
  }


  @GetMapping
  public ResponseEntity<List<AssociationUserRoleDTO>> list(
    @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
    @RequestParam(value = "roleId", required = false, defaultValue = "") Long roleId) {
    Preconditions
      .checkArgument((userId != null && roleId == null) || (userId == null && roleId != null));
    if (userId != null) {
      UserDTO user = userService.getOne(userId);
      if (user != null) {
        return new ResponseEntity<>(associationUserRoleService.getAllLeft(user), HttpStatus.OK);
      }
    }
    if (roleId != null) {
      RoleDTO role = roleService.getOne(roleId);
      if (role != null) {
        return new ResponseEntity<>(associationUserRoleService.getAllRight(role), HttpStatus.OK);
      }
    }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PostMapping
  public ResponseEntity<AssociationUserRoleDTO> associate(
    @Valid @RequestBody AssociationUserRoleForm form) {
    UserDTO userDTO = userService.getOne(form.getUserId());
    RoleDTO roleDTO = roleService.getOne(form.getRoleId());
    if (userDTO == null || roleDTO == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(associationUserRoleService.associate(userDTO, roleDTO),
      HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AssociationUserRoleDTO> get(@PathVariable Long id) {
    Preconditions.checkArgument(id != null);
    AssociationUserRoleDTO association = associationUserRoleService.getOne(id);
    if (association == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(association, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> dissociate(@PathVariable Long id) {
    Preconditions.checkArgument(id != null);
    AssociationUserRoleDTO association = associationUserRoleService.getOne(id);
    if (association != null) {
      associationUserRoleService.dissociate(association.getA(), association.getB());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping
  public ResponseEntity<Void> dissociate(@Valid @RequestBody AssociationUserRoleForm form) {
    UserDTO userDTO = userService.getOne(form.getUserId());
    RoleDTO roleDTO = roleService.getOne(form.getRoleId());
    if (userDTO == null || roleDTO == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    associationUserRoleService.dissociate(userDTO, roleDTO);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  public static class AssociationUserRoleForm {

    @NotNull
    private Long userId;
    @NotNull
    private Long roleId;

    public Long getUserId() {
      return userId;
    }

    public void setUserId(Long userId) {
      this.userId = userId;
    }

    public Long getRoleId() {
      return roleId;
    }

    public void setRoleId(Long roleId) {
      this.roleId = roleId;
    }
  }
}
