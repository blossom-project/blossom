package com.blossom_project.core.association_user_group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.blossom_project.core.common.dto.AbstractAssociationDTO;
import com.blossom_project.core.group.GroupDTO;
import com.blossom_project.core.user.UserDTO;

public class AssociationUserGroupDTO extends AbstractAssociationDTO<UserDTO, GroupDTO> {

  @Override
  @JsonProperty("user")
  public UserDTO getA() {
    return super.getA();
  }

  @Override
  @JsonProperty("group")
  public GroupDTO getB() {
    return super.getB();
  }
}
