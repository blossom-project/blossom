package fr.mgargadennec.blossom.core.association.user.role.service.dto;

import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;

public class BlossomAssociationUserRoleServiceDTO extends BlossomAbstractServiceDTO {

  private Long userId;
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
