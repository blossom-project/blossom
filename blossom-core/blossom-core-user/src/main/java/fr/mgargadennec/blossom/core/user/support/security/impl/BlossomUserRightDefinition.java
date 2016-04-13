package fr.mgargadennec.blossom.core.user.support.security.impl;

import java.util.List;

import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.user.constants.BlossomUserConst;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomRightDefinition;

public class BlossomUserRightDefinition implements IBlossomRightDefinition {

  private List<BlossomRightPermissionEnum> availablePermissions = Lists.newArrayList(BlossomRightPermissionEnum.CREATE,
      BlossomRightPermissionEnum.READ, BlossomRightPermissionEnum.WRITE, BlossomRightPermissionEnum.DELETE);

  public boolean supports(String delimiter) {
    return getRightName().equals(delimiter);
  }

  public String getRightName() {
    return BlossomUserConst.BLOSSOM_USER_RIGHT_NAME;
  }

  public List<BlossomRightPermissionEnum> getAvailablePermissions() {
    return availablePermissions;
  }

  public boolean isAssociation() {
    return false;
  }

}
