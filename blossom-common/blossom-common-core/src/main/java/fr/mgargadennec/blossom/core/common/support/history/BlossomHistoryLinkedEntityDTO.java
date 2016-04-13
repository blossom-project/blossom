package fr.mgargadennec.blossom.core.common.support.history;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;

public class BlossomHistoryLinkedEntityDTO<E extends BlossomAbstractEntity, L extends BlossomAbstractEntity> extends BlossomHistoryDTO<E> {

  private String linkedEntityId;
  private String linkedClass;
  private String linkedName;
  private L linkedEntity;

  public BlossomHistoryLinkedEntityDTO() {
    super(false, true);
  }

  public String getLinkedEntityId() {
    return linkedEntityId;
  }

  public void setLinkedEntityId(String linkedEntityId) {
    this.linkedEntityId = linkedEntityId;
  }

  public String getLinkedClass() {
    return linkedClass;
  }

  public void setLinkedClass(String linkedClass) {
    this.linkedClass = linkedClass;
  }

  public String getLinkedName() {
    return linkedName;
  }

  public void setLinkedName(String linkedName) {
    this.linkedName = linkedName;
  }

  public L getLinkedEntity() {
    return linkedEntity;
  }

  public void setLinkedEntity(L linkedEntity) {
    this.linkedEntity = linkedEntity;
  }
}
