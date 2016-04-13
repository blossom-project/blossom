package fr.mgargadennec.blossom.core.association.group.user.service.dto;

import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;

public class BlossomAssociationGroupUserServiceDTO extends BlossomAbstractServiceDTO {

  private Long groupId;
  private Long userId;

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

}
