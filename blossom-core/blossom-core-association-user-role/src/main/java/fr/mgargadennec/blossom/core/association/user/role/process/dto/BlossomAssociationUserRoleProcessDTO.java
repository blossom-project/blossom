package fr.mgargadennec.blossom.core.association.user.role.process.dto;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;

public class BlossomAssociationUserRoleProcessDTO extends BlossomAbstractProcessDTO {

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
