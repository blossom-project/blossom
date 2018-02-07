package com.blossom_project.core.association_user_role;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.blossom_project.core.common.dto.AbstractAssociationDTO;
import com.blossom_project.core.role.RoleDTO;
import com.blossom_project.core.user.UserDTO;

public class AssociationUserRoleDTO extends AbstractAssociationDTO<UserDTO, RoleDTO> {

  @Override
  @JsonProperty("user")
  public UserDTO getA() {
    return super.getA();
  }

  @Override
  @JsonProperty("role")
  public RoleDTO getB() {
    return super.getB();
  }
}
