package fr.blossom.core.association_user_role;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.blossom.core.common.dto.AbstractAssociationDTO;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.user.UserDTO;

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
