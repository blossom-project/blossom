package fr.mgargadennec.blossom.core.group.support.entity.definition.impl;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.core.group.constants.BlossomGroupConst;
import fr.mgargadennec.blossom.core.group.model.BlossomGroupPO;

public class BlossomGroupEntityDefinition implements IBlossomEntityDefinition {

  private String[] rightNames = new String[]{BlossomGroupConst.BLOSSOM_GROUP_RIGHT_NAME};

  public boolean supports(String delimiter) {
    return getEntityName().equals(delimiter);
  }

  public Class<? extends BlossomAbstractEntity> getEntityClass() {
    return BlossomGroupPO.class;
  }

  public String getEntityName() {
    return BlossomGroupConst.BLOSSOM_GROUP_ENTITY_NAME;
  }

  public boolean isAssociation() {
    return false;
  }

  public String[] getRightNames() {
    return rightNames;
  }

}
