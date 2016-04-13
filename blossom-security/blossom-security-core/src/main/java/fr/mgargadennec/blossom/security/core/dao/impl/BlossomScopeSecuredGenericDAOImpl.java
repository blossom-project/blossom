package fr.mgargadennec.blossom.security.core.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import com.mysema.query.types.Predicate;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomSecurityUtilService;

public abstract class BlossomScopeSecuredGenericDAOImpl<T extends BlossomAbstractEntity> extends BlossomSecuredGenericDAOImpl<T> {

	  protected IBlossomAuthenticationUtilService boAuthenticationUtilService;
	  protected IBlossomSecurityUtilService boSecurityUtilService;
	  
	public BlossomScopeSecuredGenericDAOImpl(BlossomJpaRepository<T> jpaRepository, IBlossomAuthenticationUtilService boAuthenticationUtilService,IBlossomSecurityUtilService boSecurityUtilService) {
		super(jpaRepository);
		this.boAuthenticationUtilService=boAuthenticationUtilService;
		this.boSecurityUtilService=boSecurityUtilService;
	}

  protected Class<T> entityClass;

  public BlossomScopeSecuredGenericDAOImpl(Class<T> entityClass, BlossomJpaRepository<T> repository) {
    super(repository);
    this.entityClass = entityClass;
  }

  public Page<T> getAll(Pageable pageable) {
    return repository.findAll(getSecuredPredicate(), pageable);
  }

  @PreAuthorize("hasPermission(#id, #secUtils.getScopeResourceNameFromClass(this.entityClass),#secUtils.read())")
  @Override
  public T get(@P("id") Long id) {
    T result = repository.findOne(id);
    if (result == null) {
      throw new EntityNotFoundException();
    }
    return result;
  }

  @PreAuthorize("hasPermission(#po,#secUtils.write())")
  public T update(@P("po") T po) {
    return super.update(po);
  }

  @PreAuthorize("hasPermission(#id, #secUtils.getScopeResourceNameFromClass(this.entityClass), #secUtils.delete())")
  @Override
  public void delete(@P("id") Long id) {
    super.delete(id);
  }

  protected Predicate getSecuredPredicate() {
    return boAuthenticationUtilService.getAllPredicate(entityClass);
  }

  public Class<T> getEntityClass() {
    return entityClass;
  }

  public void setBoAuthenticationUtilService(IBlossomAuthenticationUtilService boAuthenticationUtilService) {
    this.boAuthenticationUtilService = boAuthenticationUtilService;
  }

  public void setBoSecurityUtilService(IBlossomSecurityUtilService boSecurityUtilService) {
    this.boSecurityUtilService = boSecurityUtilService;
  }
}
