package fr.mgargadennec.blossom.core.association.group.user.support.security.impl;

import java.util.List;

import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.association.group.user.constants.BlossomAssociationGroupUserConst;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomRightDefinition;

public class BlossomAssociationGroupUserRightDefinition implements IBlossomRightDefinition {
  private List<BlossomRightPermissionEnum> availablePermissions = Lists.newArrayList(BlossomRightPermissionEnum.CREATE,
      BlossomRightPermissionEnum.READ, BlossomRightPermissionEnum.WRITE, BlossomRightPermissionEnum.DELETE);

  public boolean supports(String delimiter) {
    return getRightName().equals(delimiter);
  }

  public String getRightName() {
    return BlossomAssociationGroupUserConst.BLOSSOM_ASSOCIATION_GROUP_USER_RIGHT_NAME;
  }

  public List<BlossomRightPermissionEnum> getAvailablePermissions() {
    return availablePermissions;
  }

  public boolean isAssociation() {
    return true;
  }

}
