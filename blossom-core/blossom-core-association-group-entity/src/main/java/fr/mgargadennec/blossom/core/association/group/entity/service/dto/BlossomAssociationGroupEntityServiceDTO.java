package fr.mgargadennec.blossom.core.association.group.entity.service.dto;

import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;

public class BlossomAssociationGroupEntityServiceDTO extends BlossomAbstractServiceDTO {

  private Long groupId;
  private Long entityId;
  private String entityType;

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

  public Long getEntityId() {
    return entityId;
  }

  public void setEntityId(Long entityId) {
    this.entityId = entityId;
  }

  public String getEntityType() {
    return entityType;
  }

  public void setEntityType(String entityType) {
    this.entityType = entityType;
  }

}
