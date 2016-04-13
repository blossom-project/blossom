package fr.mgargadennec.blossom.core.history.support.security.impl;

import java.util.List;

import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.history.constants.BlossomHistoryConst;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomRightDefinition;

public class BlossomHistoryRightDefinition implements IBlossomRightDefinition {

  private List<BlossomRightPermissionEnum> availablePermissions = Lists.newArrayList(BlossomRightPermissionEnum.READ);

  public boolean supports(String delimiter) {
    return getRightName().equals(delimiter);
  }

  public String getRightName() {
    return BlossomHistoryConst.BLOSSOM_HISTORY_RIGHT_NAME;
  }

  public List<BlossomRightPermissionEnum> getAvailablePermissions() {
    return availablePermissions;
  }

  public boolean isAssociation() {
    return false;
  }

}
