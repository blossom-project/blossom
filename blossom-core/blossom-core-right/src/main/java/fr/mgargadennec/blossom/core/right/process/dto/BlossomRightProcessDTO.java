package fr.mgargadennec.blossom.core.right.process.dto;

import java.util.List;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;

public class BlossomRightProcessDTO extends BlossomAbstractProcessDTO {

  private List<BlossomRightPermissionEnum> permissions;
  private String resourceName;
  private Long roleId;

  public List<BlossomRightPermissionEnum> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<BlossomRightPermissionEnum> permissions) {
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
