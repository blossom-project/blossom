package fr.mgargadennec.blossom.core.group.support.history.builder.impl;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.history.builder.BlossomSimpleEntityAbstractHistoryBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.group.model.BlossomGroupPO;

public class BlossomGroupHistoryBuilderImpl extends BlossomSimpleEntityAbstractHistoryBuilder {

  public BlossomGroupHistoryBuilderImpl(IBlossomEntityDiffBuilder diffBuilder) {
    super(diffBuilder);
  }

  public boolean supports(Class<? extends BlossomAbstractEntity> delimiter) {
    return BlossomGroupPO.class.isAssignableFrom(delimiter);
  }

}
