package fr.mgargadennec.blossom.core.role.support.indexation.builder.impl;

import fr.mgargadennec.blossom.core.common.support.indexation.builder.BlossomIndexInfosDTO;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.role.constants.BlossomRoleConst;

public class BlossomRoleESResourceIndexBuilderImpl implements IBlossomESResourceIndexBuilder {

  public boolean supports(String delimiter) {
    return BlossomRoleConst.BLOSSOM_ROLE_ENTITY_NAME.equals(delimiter);
  }

  public BlossomIndexInfosDTO getIndexInfos() {
    return new BlossomIndexInfosDTO("roles", BlossomRoleConst.BLOSSOM_ROLE_ENTITY_NAME, false);
  }
}
