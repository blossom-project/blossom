package fr.mgargadennec.blossom.core.role.support.history.builder.impl;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.history.builder.BlossomSimpleEntityAbstractHistoryBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.role.model.BlossomRolePO;

public class BlossomRoleHistoryBuilderImpl extends BlossomSimpleEntityAbstractHistoryBuilder {

  public BlossomRoleHistoryBuilderImpl(IBlossomEntityDiffBuilder diffBuilder) {
    super(diffBuilder);
  }

  public boolean supports(Class<? extends BlossomAbstractEntity> delimiter) {
    return BlossomRolePO.class.isAssignableFrom(delimiter);
  }

}
