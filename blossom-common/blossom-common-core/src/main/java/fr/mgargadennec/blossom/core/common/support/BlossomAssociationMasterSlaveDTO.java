package fr.mgargadennec.blossom.core.common.support;

public class BlossomAssociationMasterSlaveDTO {
  private Long masterId;
  private Long slaveId;

  public BlossomAssociationMasterSlaveDTO(Long masterId, Long slaveId) {
    this.masterId = masterId;
    this.slaveId = slaveId;
  }

  public Long getMasterId() {
    return masterId;
  }

  public Long getSlaveId() {
    return slaveId;
  }
}