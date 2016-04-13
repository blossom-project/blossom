/**
 *
 */
package fr.mgargadennec.blossom.core.user.support.history.builder.impl;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.history.builder.BlossomSimpleEntityAbstractHistoryBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.user.model.BlossomUserPO;


public class BlossomUserHistoryBuilderImpl extends BlossomSimpleEntityAbstractHistoryBuilder {

  public BlossomUserHistoryBuilderImpl(IBlossomEntityDiffBuilder diffBuilder) {
    super(diffBuilder);
  }

  public boolean supports(Class<? extends BlossomAbstractEntity> delimiter) {
    return BlossomUserPO.class.isAssignableFrom(delimiter);
  }

}
