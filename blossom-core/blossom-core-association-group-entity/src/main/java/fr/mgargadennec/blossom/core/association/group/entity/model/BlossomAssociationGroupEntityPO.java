package fr.mgargadennec.blossom.core.association.group.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.group.model.BlossomGroupPO;

@Entity(name = "BlossomAssociationGroupEntity")
@Table(name = "BLOSSOM_ASSO_GROUP_ENTITY")
@Audited
public class BlossomAssociationGroupEntityPO extends BlossomAbstractEntity {

  @Column(name = "GROUP_ID")
  private Long groupId;

  @Column(name = "ENTITY_ID")
  private Long entityId;

  @Column(name = "ENTITY_TYPE")
  private String entityType;

  @ManyToOne
  @JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @Transient
  private BlossomGroupPO group;

  public Long getGroupId() {
    return groupId;
  }

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
