package fr.mgargadennec.blossom.core.common.support.entity.definition.impl;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;

public class BlossomHistoryEntityDefinition implements IBlossomEntityDefinition {

  private String[] rightNames = new String[]{BlossomConst.BO_HISTORY_RIGHT_NAME};

  public boolean supports(String delimiter) {
    return getEntityName().equals(delimiter);
  }

  public Class<? extends BlossomAbstractEntity> getEntityClass() {
    return null;
  }

  public String getEntityName() {
    return BlossomConst.BLOSSOM_HISTORY_ENTITY_NAME;
  }

  public boolean isAssociation() {
    return false;
  }

  public String[] getRightNames() {
    return rightNames;
  }

}
