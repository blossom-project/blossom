package fr.mgargadennec.blossom.core.association.user.role.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;

@Entity(name = "BlossomAssociationUserRole")
@Table(name = "BLOSSOM_ASSO_USER_ROLE")
@Audited
public class BlossomAssociationUserRolePO extends BlossomAbstractEntity {

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "ROLE_ID")
  private Long roleId;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long groupId) {
    this.userId = groupId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }
}
