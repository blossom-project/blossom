package fr.mgargadennec.blossom.core.group.support.security.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.group.constants.BlossomGroupConst;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomRightDefinition;

public class BlossomGroupRightDefinition implements IBlossomRightDefinition {

  private ArrayList<BlossomRightPermissionEnum> availablePermissions = Lists.newArrayList(BlossomRightPermissionEnum.CREATE,
      BlossomRightPermissionEnum.READ, BlossomRightPermissionEnum.WRITE, BlossomRightPermissionEnum.DELETE);

  public boolean supports(String delimiter) {
    return getRightName().equals(delimiter);
  }

  public String getRightName() {
    return BlossomGroupConst.BLOSSOM_GROUP_RIGHT_NAME;
  }

  public List<BlossomRightPermissionEnum> getAvailablePermissions() {
    return availablePermissions;
  }

  public boolean isAssociation() {
    return false;
  }

}
