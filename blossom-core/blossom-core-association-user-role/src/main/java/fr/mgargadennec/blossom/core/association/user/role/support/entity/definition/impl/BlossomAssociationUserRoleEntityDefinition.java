package fr.mgargadennec.blossom.core.association.user.role.support.entity.definition.impl;

import fr.mgargadennec.blossom.core.association.user.role.constants.BlossomAssociationUserRoleConst;
import fr.mgargadennec.blossom.core.association.user.role.model.BlossomAssociationUserRolePO;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;

public class BlossomAssociationUserRoleEntityDefinition implements IBlossomEntityDefinition {
  private String[] rightNames = new String[]{BlossomAssociationUserRoleConst.BO_ASSOCIATION_USER_ROLE_RIGHT_NAME};

  public boolean supports(String delimiter) {
    return getEntityName().equals(delimiter);
  }

  public Class<? extends BlossomAbstractEntity> getEntityClass() {
    return BlossomAssociationUserRolePO.class;
  }

  public String getEntityName() {
    return BlossomAssociationUserRoleConst.BLOSSOM_ASSOCIATION_USER_ROLE_ENTITY_NAME;
  }

  public boolean isAssociation() {
    return true;
  }

  public String[] getRightNames() {
    return rightNames;
  }

}
