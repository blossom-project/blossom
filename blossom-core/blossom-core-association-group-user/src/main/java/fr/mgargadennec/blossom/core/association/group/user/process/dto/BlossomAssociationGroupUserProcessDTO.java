package fr.mgargadennec.blossom.core.association.group.user.process.dto;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;

public class BlossomAssociationGroupUserProcessDTO extends BlossomAbstractProcessDTO {

  private Long groupId;
  private Long userId;

  /**
   * @return the groupId
   */
  public Long getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the userId
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

}
