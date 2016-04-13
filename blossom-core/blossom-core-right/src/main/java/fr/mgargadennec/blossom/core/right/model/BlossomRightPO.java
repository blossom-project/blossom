package fr.mgargadennec.blossom.core.right.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.core.right.constants.BlossomRightConst;

@Entity(name = "BlossomRight")
@Table(name = "BLOSSOM_RIGHT")
@Audited
@BlossomResourceType(BlossomRightConst.BLOSSOM_RIGHT_ENTITY_NAME)
public class BlossomRightPO extends BlossomAbstractEntity {

  @Column(name = "PERMISSIONS")
  private String permissions;

  @Column(name = "RESOURCE")
  private String resourceName;

  @Column(name = "ROLE_ID")
  private Long roleId;

  public String getPermissions() {
    return permissions;
  }

  public void setPermissions(String permissions) {
    this.permissions = permissions;
  }

  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

}
