package fr.blossom.core.association_user_group;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.blossom.core.common.dto.AbstractAssociationDTO;
import fr.blossom.core.group.GroupDTO;
import fr.blossom.core.user.UserDTO;

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
