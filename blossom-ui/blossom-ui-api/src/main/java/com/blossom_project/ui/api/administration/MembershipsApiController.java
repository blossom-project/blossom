package com.blossom_project.ui.api.administration;

import com.google.common.base.Preconditions;
import com.blossom_project.core.association_user_group.AssociationUserGroupDTO;
import com.blossom_project.core.association_user_group.AssociationUserGroupService;
import com.blossom_project.core.group.GroupDTO;
import com.blossom_project.core.group.GroupService;
import com.blossom_project.core.user.UserDTO;
import com.blossom_project.core.user.UserService;
import com.blossom_project.ui.stereotype.BlossomApiController;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/administration/memberships")
public class MembershipsApiController {

  private final AssociationUserGroupService associationUserGroupService;
  private final UserService userService;
  private final GroupService groupService;

  public MembershipsApiController(AssociationUserGroupService associationUserGroupService,
    UserService userService, GroupService groupService) {
    this.associationUserGroupService = associationUserGroupService;
    this.userService = userService;
    this.groupService = groupService;
  }


  @GetMapping
  @PreAuthorize("hasAuthority('administration:memberships:read')")
  public ResponseEntity<List<AssociationUserGroupDTO>> list(
    @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
    @RequestParam(value = "groupId", required = false, defaultValue = "") Long groupId) {
    Preconditions
      .checkArgument((userId != null && groupId == null) || (userId == null && groupId != null));
    if (userId != null) {
      UserDTO user = userService.getOne(userId);
      if (user != null) {
        return new ResponseEntity<>(associationUserGroupService.getAllLeft(user), HttpStatus.OK);
      }
    }
    if (groupId != null) {
      GroupDTO group = groupService.getOne(groupId);
      if (group != null) {
        return new ResponseEntity<>(associationUserGroupService.getAllRight(group), HttpStatus.OK);
      }
    }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('administration:memberships:change')")
  public ResponseEntity<AssociationUserGroupDTO> associate(
    @Valid @RequestBody AssociationUserGroupForm form) {
    UserDTO userDTO = userService.getOne(form.getUserId());
    GroupDTO groupDTO = groupService.getOne(form.getGroupId());
    if (userDTO == null || groupDTO == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(associationUserGroupService.associate(userDTO, groupDTO),
      HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:memberships:read')")
  public ResponseEntity<AssociationUserGroupDTO> get(@PathVariable Long id) {
    Preconditions.checkArgument(id != null);
    AssociationUserGroupDTO association = associationUserGroupService.getOne(id);
    if (association == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(association, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('administration:memberships:change')")
  public ResponseEntity<Void> dissociate(@PathVariable Long id) {
    Preconditions.checkArgument(id != null);
    AssociationUserGroupDTO association = associationUserGroupService.getOne(id);
    if (association != null) {
      associationUserGroupService.dissociate(association.getA(), association.getB());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('administration:memberships:change')")
  public ResponseEntity<Void> dissociate(@Valid @RequestBody AssociationUserGroupForm form) {
    UserDTO userDTO = userService.getOne(form.getUserId());
    GroupDTO groupDTO = groupService.getOne(form.getGroupId());
    if (userDTO == null || groupDTO == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    associationUserGroupService.dissociate(userDTO, groupDTO);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  public static class AssociationUserGroupForm {

    @NotNull
    private Long userId;
    @NotNull
    private Long groupId;

    public Long getUserId() {
      return userId;
    }

    public void setUserId(Long userId) {
      this.userId = userId;
    }

    public Long getGroupId() {
      return groupId;
    }

    public void setGroupId(Long groupId) {
      this.groupId = groupId;
    }
  }
}
