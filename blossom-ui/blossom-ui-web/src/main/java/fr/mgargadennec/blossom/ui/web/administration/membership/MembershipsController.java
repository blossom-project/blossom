package fr.mgargadennec.blossom.ui.web.administration.membership;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupDTO;
import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupService;
import fr.mgargadennec.blossom.core.association_user_group.UpdateAssociationUserGroupForm;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
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
import org.springframework.web.servlet.ModelAndView;

@BlossomController("/administration/memberships")
public class MembershipsController {

  private final AssociationUserGroupService associationUserGroupService;
  private final UserService userService;
  private final SearchEngineImpl<GroupDTO> userSearchEngine;

  private final GroupService groupService;
  private final SearchEngineImpl<GroupDTO> groupSearchEngine;

  public MembershipsController(AssociationUserGroupService associationUserGroupService,
    UserService userService, SearchEngineImpl<GroupDTO> userSearchEngine, GroupService groupService,
    SearchEngineImpl<GroupDTO> groupSearchEngine) {
    this.associationUserGroupService = associationUserGroupService;
    this.userService = userService;
    this.userSearchEngine = userSearchEngine;
    this.groupService = groupService;
    this.groupSearchEngine = groupSearchEngine;
  }

  @GetMapping("/users/{id}/groups")
  @PreAuthorize("hasAuthority('administration:memberships:read')")
  public ModelAndView getUserMembershipsPage(@PathVariable Long id, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    List<AssociationUserGroupDTO> associations = associationUserGroupService.getAllLeft(user);

    return this.membershipsView(new PageImpl<>(associations), "user");
  }

  @GetMapping("/users/{id}/groups/_edit")
  @PreAuthorize("hasAuthority('administration:memberships:change')")
  public ModelAndView getUserMembershipsForm(@PathVariable Long id, Model model) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }
    return this
      .membershipsForm(associationUserGroupService.getAllLeft(user), user, null, "user", model);
  }

  @Transactional
  @PostMapping("/users/{id}/groups/_edit")
  @PreAuthorize("hasAuthority('administration:memberships:change')")
  public ModelAndView handleUserMembershipsForm(@PathVariable Long id, Model model,
    @ModelAttribute("form") UpdateAssociationUserGroupForm updateMembershipsForm) {
    UserDTO user = this.userService.getOne(id);
    if (user == null) {
      throw new NoSuchElementException(String.format("User=%s not found", id));
    }

    Set<Long> groupIds = updateMembershipsForm.getIds();
    List<AssociationUserGroupDTO> associations = associationUserGroupService.getAllLeft(user);
    Set<Long> actualGroupIds = associations.stream().map(association -> association.getB().getId())
      .collect(
        Collectors.toSet());

    Set<Long> toDelete = Sets.difference(actualGroupIds, groupIds);
    List<GroupDTO> toDeleteGroups = groupService.getAll(Lists.newArrayList(toDelete));
    for (GroupDTO toDeleteGroup : toDeleteGroups) {
      associationUserGroupService.dissociate(user, toDeleteGroup);
    }

    Set<Long> toCreate = Sets.difference(groupIds, actualGroupIds);
    List<GroupDTO> toCreateGroups = groupService.getAll(Lists.newArrayList(toCreate));
    for (GroupDTO toCreateGroup : toCreateGroups) {
      associationUserGroupService.associate(user, toCreateGroup);
    }

    associations = associationUserGroupService.getAllLeft(user);
    return this.membershipsView(new PageImpl<>(associations), "user");
  }

  @GetMapping("/groups/{id}/users")
  @PreAuthorize("hasAuthority('administration:memberships:read')")
  public ModelAndView getGroupMembershipsPage(@PathVariable Long id, Model model) {
    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      throw new NoSuchElementException(String.format("Group=%s not found", id));
    }
    List<AssociationUserGroupDTO> associations = associationUserGroupService.getAllRight(group);

    return this.membershipsView(new PageImpl<>(associations), "group");
  }

  @GetMapping("/groups/{id}/users/_edit")
  @PreAuthorize("hasAuthority('administration:memberships:change')")
  public ModelAndView getGroupMembershipsForm(@PathVariable Long id, Model model) {
    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      throw new NoSuchElementException(String.format("Group=%s not found", id));
    }
    return this
      .membershipsForm(associationUserGroupService.getAllRight(group), null, group, "group", model);
  }

  @Transactional
  @PostMapping("/groups/{id}/users/_edit")
  @PreAuthorize("hasAuthority('administration:memberships:change')")
  public ModelAndView handleGroupMembershipsForm(@PathVariable Long id, Model model,
    @ModelAttribute("form") UpdateAssociationUserGroupForm updateAssociationUserGroupForm) {
    GroupDTO group = this.groupService.getOne(id);
    if (group == null) {
      throw new NoSuchElementException(String.format("Group=%s not found", id));
    }

    Set<Long> userIds = updateAssociationUserGroupForm.getIds();
    List<AssociationUserGroupDTO> associations = associationUserGroupService.getAllRight(group);
    Set<Long> actualUserIds = associations.stream().map(association -> association.getA().getId())
      .collect(
        Collectors.toSet());

    Set<Long> toDelete = Sets.difference(actualUserIds, userIds);
    List<UserDTO> toDeleteUsers = userService.getAll(Lists.newArrayList(toDelete));
    for (UserDTO toDeleteUser : toDeleteUsers) {
      associationUserGroupService.dissociate(toDeleteUser, group);
    }

    Set<Long> toCreate = Sets.difference(userIds, actualUserIds);
    List<UserDTO> toCreateUsers = userService.getAll(Lists.newArrayList(toCreate));
    for (UserDTO toCreateUser : toCreateUsers) {
      associationUserGroupService.associate(toCreateUser, group);
    }

    associations = associationUserGroupService.getAllRight(group);
    return this.membershipsView(new PageImpl<>(associations), "group");
  }


  private ModelAndView membershipsView(Page<AssociationUserGroupDTO> memberships, String mode) {
    return new ModelAndView("memberships/memberships-" + mode, "memberships", memberships);
  }

  private ModelAndView membershipsForm(List<AssociationUserGroupDTO> memberships, UserDTO user,
    GroupDTO group, String mode, Model model) {
    model.addAttribute("associations", memberships);
    model.addAttribute("users", userService.getAll());
    model.addAttribute("groups", groupService.getAll());
    model.addAttribute("user", user);
    model.addAttribute("group", group);
    return new ModelAndView("memberships/memberships-" + mode + "-edit", model.asMap());
  }
}
