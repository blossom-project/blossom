package fr.mgargadennec.blossom.ui.web.administration.membership;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.mgargadennec.blossom.core.association_user_group.AssociationUserGroupService;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupService;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;

@BlossomController("/administration/memberships")
public class MembershipsController {

  private final UserService userService;
  private final AssociationUserGroupService associationUserGroupService;
  private final GroupService groupService;

  public MembershipsController(UserService userService, AssociationUserGroupService associationUserGroupService,
      GroupService groupService) {
    this.userService = userService;
    this.associationUserGroupService = associationUserGroupService;
    this.groupService = groupService;
  }

  @PostMapping
  @ResponseBody
  public void handleMembershipToGroupCreation(Long userId, Long groupId, Model model) {
    UserDTO user = this.userService.getOne(userId);
    GroupDTO group = this.groupService.getOne(groupId);

    this.associationUserGroupService.associate(user, group);

  }

  @DeleteMapping
  @ResponseBody
  public void handleMembershipToGroupDeletion(Long userId, Long groupId, Model model) {
    UserDTO user = this.userService.getOne(userId);
    GroupDTO group = this.groupService.getOne(groupId);

    this.associationUserGroupService.dissociate(user, group);

  }

}
