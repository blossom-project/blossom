package fr.mgargadennec.blossom.core.right.support.entity.definition.impl;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.core.right.constants.BlossomRightConst;
import fr.mgargadennec.blossom.core.right.model.BlossomRightPO;

public class BlossomRightEntityDefinition implements IBlossomEntityDefinition {

  private String[] rightNames = new String[]{BlossomRightConst.BLOSSOM_RIGHT_RIGHT_NAME};

  public boolean supports(String delimiter) {
    return BlossomRightConst.BLOSSOM_RIGHT_ENTITY_NAME.equals(delimiter);
  }

  public Class<? extends BlossomAbstractEntity> getEntityClass() {
    return BlossomRightPO.class;
  }

  public String getEntityName() {
    return BlossomRightConst.BLOSSOM_RIGHT_ENTITY_NAME;
  }

  public boolean isAssociation() {
    return true;
  }

  public String[] getRightNames() {
    return rightNames;
  }
}
