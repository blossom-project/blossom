package fr.mgargadennec.blossom.core.common.support.history;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;

public class BlossomHistoryAssociationDTO<E extends BlossomAbstractEntity, M extends BlossomAbstractEntity, S extends BlossomAbstractEntity>
    extends BlossomHistoryDTO<E> {

  private String masterEntityId;
  private String slaveEntityId;

  private String masterClass;
  private String slaveClass;

  private String masterName;
  private String slaveName;

  private M masterEntity;
  private S slaveEntity;

  public BlossomHistoryAssociationDTO() {
    super(true, false);
  }

  public M getMasterEntity() {
    return masterEntity;
  }

  public void setMasterEntity(M masterEntity) {
    this.masterEntity = masterEntity;
  }

  public S getSlaveEntity() {
    return slaveEntity;
  }

  public void setSlaveEntity(S slaveEntity) {
    this.slaveEntity = slaveEntity;
  }

  public String getMasterClass() {
    return masterClass;
  }

  public void setMasterClass(String masterClass) {
    this.masterClass = masterClass;
  }

  public String getSlaveClass() {
    return slaveClass;
  }

  public void setSlaveClass(String slaveClass) {
    this.slaveClass = slaveClass;
  }

  public String getMasterName() {
    return masterName;
  }

  public void setMasterName(String masterName) {
    this.masterName = masterName;
  }

  public String getSlaveName() {
    return slaveName;
  }

  public void setSlaveName(String slaveName) {
    this.slaveName = slaveName;
  }

  public String getMasterEntityId() {
    return masterEntityId;
  }

  public void setMasterEntityId(String masterEntityId) {
    this.masterEntityId = masterEntityId;
  }

  public String getSlaveEntityId() {
    return slaveEntityId;
  }

  public void setSlaveEntityId(String slaveEntityId) {
    this.slaveEntityId = slaveEntityId;
  }
}
