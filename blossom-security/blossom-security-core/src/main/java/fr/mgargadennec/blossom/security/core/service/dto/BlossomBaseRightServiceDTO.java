package fr.mgargadennec.blossom.security.core.service.dto;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;

public class BlossomBaseRightServiceDTO extends BlossomAbstractServiceDTO implements GrantedAuthority {

  private static final long serialVersionUID = 1L;
  private List<BlossomRightPermissionEnum> permissions;
  private String resource;
  private Long roleId;

  public List<BlossomRightPermissionEnum> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<BlossomRightPermissionEnum> permissions) {
    this.permissions = permissions;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public String getAuthority() {
    return null;
  }

  @Override
  public String toString() {
    return "[" + getId() + " "
        + ((permissions != null && !permissions.isEmpty()) ? permissions.toString() : "[ZERO_PERM]") + " "
        + resource + "]";
  }

}
