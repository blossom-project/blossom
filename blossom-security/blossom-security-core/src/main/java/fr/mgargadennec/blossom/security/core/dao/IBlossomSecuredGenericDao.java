package fr.mgargadennec.blossom.security.core.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.dao.generic.IBlossomGenericDao;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;

public interface IBlossomSecuredGenericDao<T extends BlossomAbstractEntity> extends IBlossomGenericDao<T> {

  @Secured({BlossomConst.SECURITY_GENERIC_DAO_READ})
  Page<T> getAll(Pageable pageable);

  @Secured({BlossomConst.SECURITY_GENERIC_DAO_READ})
  T get(Long id);

  @Secured({BlossomConst.SECURITY_GENERIC_DAO_CREATE})
  T create(T po);

  @Secured({BlossomConst.SECURITY_GENERIC_DAO_WRITE})
  T update(T po);

  @Secured({BlossomConst.SECURITY_GENERIC_DAO_DELETE})
  void delete(Long id);

  @Secured({BlossomConst.SECURITY_GENERIC_DAO_READ})
  boolean exists(Long id);
}
