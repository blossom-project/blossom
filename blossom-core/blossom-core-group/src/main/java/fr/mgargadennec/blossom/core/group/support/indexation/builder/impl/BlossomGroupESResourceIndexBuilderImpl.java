package fr.mgargadennec.blossom.core.group.support.indexation.builder.impl;

import fr.mgargadennec.blossom.core.common.support.indexation.builder.BlossomIndexInfosDTO;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.group.constants.BlossomGroupConst;

public class BlossomGroupESResourceIndexBuilderImpl implements IBlossomESResourceIndexBuilder {

  public boolean supports(String delimiter) {
    return BlossomGroupConst.BLOSSOM_GROUP_ENTITY_NAME.equals(delimiter);
  }

  public BlossomIndexInfosDTO getIndexInfos() {
    return new BlossomIndexInfosDTO("groups", BlossomGroupConst.BLOSSOM_GROUP_ENTITY_NAME, false);
  }

}
