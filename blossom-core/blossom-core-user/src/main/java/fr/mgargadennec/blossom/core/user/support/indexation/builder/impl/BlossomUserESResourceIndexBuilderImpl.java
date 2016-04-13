package fr.mgargadennec.blossom.core.user.support.indexation.builder.impl;

import fr.mgargadennec.blossom.core.common.support.indexation.builder.BlossomIndexInfosDTO;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.user.constants.BlossomUserConst;

public class BlossomUserESResourceIndexBuilderImpl implements IBlossomESResourceIndexBuilder {

  public boolean supports(String delimiter) {
    return BlossomUserConst.BLOSSOM_USER_ENTITY_NAME.equals(delimiter);
  }

  public BlossomIndexInfosDTO getIndexInfos() {
    return new BlossomIndexInfosDTO("users", BlossomUserConst.BLOSSOM_USER_ENTITY_NAME, false);
  }
}
