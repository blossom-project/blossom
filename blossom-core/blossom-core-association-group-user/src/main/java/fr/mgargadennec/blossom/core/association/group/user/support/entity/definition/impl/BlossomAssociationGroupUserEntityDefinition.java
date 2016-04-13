package fr.mgargadennec.blossom.core.association.group.user.support.entity.definition.impl;

import fr.mgargadennec.blossom.core.association.group.user.constants.BlossomAssociationGroupUserConst;
import fr.mgargadennec.blossom.core.association.group.user.model.BlossomAssociationGroupUserPO;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;

public class BlossomAssociationGroupUserEntityDefinition implements IBlossomEntityDefinition {
  private String[] rightNames = new String[]{BlossomAssociationGroupUserConst.BLOSSOM_ASSOCIATION_GROUP_USER_RIGHT_NAME};

  public boolean supports(String delimiter) {
    return getEntityName().equals(delimiter);
  }

  public Class<? extends BlossomAbstractEntity> getEntityClass() {
    return BlossomAssociationGroupUserPO.class;
  }

  public String getEntityName() {
    return BlossomAssociationGroupUserConst.BLOSSOM_ASSOCIATION_GROUP_USER_ENTITY_NAME;
  }

  public boolean isAssociation() {
    return true;
  }

  public String[] getRightNames() {
    return rightNames;
  }

}
