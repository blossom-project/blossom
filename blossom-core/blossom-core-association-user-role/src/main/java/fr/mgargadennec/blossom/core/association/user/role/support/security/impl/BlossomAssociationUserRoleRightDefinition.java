package fr.mgargadennec.blossom.core.association.user.role.support.security.impl;

import java.util.List;

import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.association.user.role.constants.BlossomAssociationUserRoleConst;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomRightDefinition;

public class BlossomAssociationUserRoleRightDefinition implements IBlossomRightDefinition {
  private List<BlossomRightPermissionEnum> availablePermissions = Lists.newArrayList(BlossomRightPermissionEnum.CREATE,
      BlossomRightPermissionEnum.READ, BlossomRightPermissionEnum.WRITE, BlossomRightPermissionEnum.DELETE);

  public boolean supports(String delimiter) {
    return getRightName().equals(delimiter);
  }

  public String getRightName() {
    return BlossomAssociationUserRoleConst.BO_ASSOCIATION_USER_ROLE_RIGHT_NAME;
  }

  public List<BlossomRightPermissionEnum> getAvailablePermissions() {
    return availablePermissions;
  }

  public boolean isAssociation() {
    return true;
  }

}
