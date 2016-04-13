package fr.mgargadennec.blossom.security.core.dao.impl;

import fr.mgargadennec.blossom.core.common.dao.generic.impl.BlossomGenericDAOImpl;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;
import fr.mgargadennec.blossom.security.core.dao.IBlossomSecuredGenericDao;

public class BlossomSecuredGenericDAOImpl<T extends BlossomAbstractEntity> extends BlossomGenericDAOImpl<T> implements
    IBlossomSecuredGenericDao<T> {

  public BlossomSecuredGenericDAOImpl(BlossomJpaRepository<T> repository) {
    super(repository);
  }

}
