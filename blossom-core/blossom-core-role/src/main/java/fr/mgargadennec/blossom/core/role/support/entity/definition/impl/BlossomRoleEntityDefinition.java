package fr.mgargadennec.blossom.core.role.support.entity.definition.impl;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.core.role.constants.BlossomRoleConst;
import fr.mgargadennec.blossom.core.role.model.BlossomRolePO;

public class BlossomRoleEntityDefinition implements IBlossomEntityDefinition {

  private String[] rightNames = new String[]{BlossomRoleConst.BLOSSOM_ROLE_RIGHT_NAME};

  public boolean supports(String delimiter) {
    return getEntityName().equals(delimiter);
  }

  public Class<? extends BlossomAbstractEntity> getEntityClass() {
    return BlossomRolePO.class;
  }

  public String getEntityName() {
    return BlossomRoleConst.BLOSSOM_ROLE_ENTITY_NAME;
  }

  public boolean isAssociation() {
    return false;
  }

  public String[] getRightNames() {
    return rightNames;
  }

}
