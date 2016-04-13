package fr.mgargadennec.blossom.core.association.group.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import fr.mgargadennec.blossom.core.association.group.user.constants.BlossomAssociationGroupUserConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;

@Entity(name = "BlossomAssociationGroupUser")
@Table(name = "BLOSSOM_ASSO_GROUP_USER")
@Audited
@BlossomResourceType(BlossomAssociationGroupUserConst.BLOSSOM_ASSOCIATION_GROUP_USER_ENTITY_NAME)
public class BlossomAssociationGroupUserPO extends BlossomAbstractEntity {

  @Column(name = "GROUP_ID")
  private Long groupId;

  @Column(name = "USER_ID")
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
