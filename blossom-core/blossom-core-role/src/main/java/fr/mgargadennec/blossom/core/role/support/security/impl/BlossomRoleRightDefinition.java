package fr.mgargadennec.blossom.core.role.support.security.impl;

import java.util.List;

import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.role.constants.BlossomRoleConst;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomRightDefinition;

public class BlossomRoleRightDefinition implements IBlossomRightDefinition {

  private List<BlossomRightPermissionEnum> availablePermissions = Lists.newArrayList(BlossomRightPermissionEnum.CREATE,
      BlossomRightPermissionEnum.READ, BlossomRightPermissionEnum.WRITE, BlossomRightPermissionEnum.DELETE,
      BlossomRightPermissionEnum.CLEARANCE);

  public boolean supports(String delimiter) {
    return getRightName().equals(delimiter);
  }

  public String getRightName() {
    return BlossomRoleConst.BLOSSOM_ROLE_RIGHT_NAME;
  }

  public List<BlossomRightPermissionEnum> getAvailablePermissions() {
    return availablePermissions;
  }

  public boolean isAssociation() {
    return false;
  }

}
