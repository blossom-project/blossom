package fr.mgargadennec.blossom.core.user.support.entity.definition.impl;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.core.user.constants.BlossomUserConst;
import fr.mgargadennec.blossom.core.user.model.BlossomUserPO;

public class BlossomUserEntityDefinition implements IBlossomEntityDefinition {

  private String[] rightNames = new String[]{BlossomUserConst.BLOSSOM_USER_RIGHT_NAME};

  public boolean supports(String delimiter) {
    return getEntityName().equals(delimiter);
  }

  public Class<? extends BlossomAbstractEntity> getEntityClass() {
    return BlossomUserPO.class;
  }

  public String getEntityName() {
    return BlossomUserConst.BLOSSOM_USER_ENTITY_NAME;
  }

  public boolean isAssociation() {
    return false;
  }

  public String[] getRightNames() {
    return rightNames;
  }

}
