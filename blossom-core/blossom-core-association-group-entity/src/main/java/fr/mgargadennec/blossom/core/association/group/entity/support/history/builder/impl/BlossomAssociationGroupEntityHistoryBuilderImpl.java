	/**
 *
 */
package fr.mgargadennec.blossom.core.association.group.entity.support.history.builder.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.support.Repositories;
import org.springframework.plugin.core.PluginRegistry;

import com.google.common.base.Preconditions;

import fr.mgargadennec.blossom.core.association.group.entity.model.BlossomAssociationGroupEntityPO;
import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.core.common.support.history.builder.BlossomAssociationEntityAbstractHistoryBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.group.model.BlossomGroupPO;
import fr.mgargadennec.blossom.core.group.repository.BlossomGroupRepository;

public class BlossomAssociationGroupEntityHistoryBuilderImpl<A extends BlossomAbstractEntity> extends
    BlossomAssociationEntityAbstractHistoryBuilder<BlossomGroupPO, A> implements ApplicationContextAware {
  private Logger logger = LoggerFactory.getLogger(BlossomAssociationGroupEntityHistoryBuilderImpl.class);

  private IBlossomHistoryDAO historyDao;
  private Repositories repositories;

  private PluginRegistry<IBlossomEntityDefinition, String> entityDefinitionRegistry;

  public BlossomAssociationGroupEntityHistoryBuilderImpl(IBlossomEntityDiffBuilder diffBuilder, IBlossomHistoryDAO historyDao,
      PluginRegistry<IBlossomEntityDefinition, String> entityBuilderRegistry) {
    super(diffBuilder);
    this.historyDao = historyDao;
    this.entityDefinitionRegistry = entityBuilderRegistry;
  }

  @Override
  protected <E extends BlossomAbstractEntity> Class<BlossomGroupPO> getMasterEntityClass(BlossomRevisionEntity revision, E entity) {
    return BlossomGroupPO.class;
  }

  @Override
  protected <E extends BlossomAbstractEntity> BlossomGroupPO getMasterEntity(BlossomRevisionEntity revision, E entity) {
    Preconditions.checkArgument(this.supports(entity.getClass()));
    BlossomGroupPO revisionEntity = null;
    try {
      revisionEntity = historyDao.getEntityAtPreviousRevision(BlossomGroupPO.class,
          ((BlossomAssociationGroupEntityPO) entity).getGroupId(), revision.getId());

    } catch (Exception e) {
      logger.warn("Can't fetch revision entry for entity type " + BlossomGroupPO.class + " with id "
          + ((BlossomAssociationGroupEntityPO) entity).getEntityId(), e);
    }

    // If entity is not found when revised, we try to fetch the current state
    if (revisionEntity == null) {
      if (repositories.hasRepositoryFor(BlossomGroupPO.class)) {
    	BlossomGroupRepository repository = (BlossomGroupRepository) repositories.getRepositoryFor(BlossomGroupPO.class);
        revisionEntity = repository.findOne(((BlossomAssociationGroupEntityPO) entity).getGroupId());
      }
    }

    return revisionEntity;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected <E extends BlossomAbstractEntity> Class<A> getSlaveEntityClass(BlossomRevisionEntity revision, E entity) {
    A slaveEntity = getSlaveEntity(revision, entity);
    if (slaveEntity != null) {
      return (Class<A>) slaveEntity.getClass();
    }

    String entityName = ((BlossomAssociationGroupEntityPO) entity).getEntityType();
    IBlossomEntityDefinition entityDescriptionDTO = entityDefinitionRegistry.getPluginFor(entityName);
    Class entityClass = entityDescriptionDTO.getEntityClass();
    return (Class<A>) entityClass;
  }

  @Override
  protected <E extends BlossomAbstractEntity> A getSlaveEntity(BlossomRevisionEntity revision, E entity) {
    Preconditions.checkArgument(this.supports(entity.getClass()));

    String entityName = ((BlossomAssociationGroupEntityPO) entity).getEntityType();
    IBlossomEntityDefinition entityDescriptionDTO = entityDefinitionRegistry.getPluginFor(entityName);
    Class entityClass = entityDescriptionDTO.getEntityClass();

    A revisionEntity = null;
    try {
      revisionEntity = historyDao.getEntityAtPreviousRevision((Class<A>) entityClass,
          ((BlossomAssociationGroupEntityPO) entity).getEntityId(), revision.getId());

    } catch (Exception e) {
      logger.warn("Can't fetch revision entry for entity type " + entityClass + " with id "
          + ((BlossomAssociationGroupEntityPO) entity).getEntityId(), e);
    }

    // If entity is not found when revised, we try to fetch the current state
    if (revisionEntity == null) {
      if (repositories.hasRepositoryFor(entityClass)) {
    	CrudMethods crudInvoker = repositories.getRepositoryInformationFor(entityClass).getCrudMethods();
      	if(crudInvoker.hasFindOneMethod()){
      		CrudRepository<A,Long> repository = (CrudRepository<A,Long>) repositories.getRepositoryFor((Class<A>) entityClass);
      		revisionEntity = repository.findOne(((BlossomAssociationGroupEntityPO) entity).getEntityId());
      	}
      }
    }

    return revisionEntity;
  }

  public boolean supports(Class<? extends BlossomAbstractEntity> delimiter) {
    return BlossomAssociationGroupEntityPO.class.isAssignableFrom(delimiter);
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.repositories = new Repositories(applicationContext);
  }
}
