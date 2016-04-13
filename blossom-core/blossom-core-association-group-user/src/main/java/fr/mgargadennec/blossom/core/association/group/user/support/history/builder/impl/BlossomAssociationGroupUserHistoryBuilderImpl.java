package fr.mgargadennec.blossom.core.association.group.user.support.history.builder.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import fr.mgargadennec.blossom.core.association.group.user.model.BlossomAssociationGroupUserPO;
import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.history.builder.BlossomAssociationEntityAbstractHistoryBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.group.model.BlossomGroupPO;
import fr.mgargadennec.blossom.core.group.repository.BlossomGroupRepository;
import fr.mgargadennec.blossom.core.user.model.BlossomUserPO;
import fr.mgargadennec.blossom.core.user.repository.BlossomUserRepository;

public class BlossomAssociationGroupUserHistoryBuilderImpl extends
BlossomAssociationEntityAbstractHistoryBuilder<BlossomUserPO, BlossomGroupPO> {
  private Logger logger = LoggerFactory.getLogger(BlossomAssociationGroupUserHistoryBuilderImpl.class);

  private IBlossomHistoryDAO historyDao;
  private BlossomGroupRepository blossomGroupRepository;
  private BlossomUserRepository blossomUserRepository;

  public BlossomAssociationGroupUserHistoryBuilderImpl(IBlossomEntityDiffBuilder diffBuilder, IBlossomHistoryDAO historyDao,BlossomGroupRepository blossomGroupRepository,BlossomUserRepository blossomUserRepository) {
    super(diffBuilder);
    this.historyDao = historyDao;
    this.blossomGroupRepository=blossomGroupRepository;
    this.blossomUserRepository=blossomUserRepository;
  }

  @Override
  protected <E extends BlossomAbstractEntity> Class<BlossomUserPO> getMasterEntityClass(BlossomRevisionEntity revision, E entity) {
    return BlossomUserPO.class;
  }

  @Override
  protected <E extends BlossomAbstractEntity> BlossomUserPO getMasterEntity(BlossomRevisionEntity revision, E entity) {
    Preconditions.checkArgument(this.supports(entity.getClass()));

    BlossomUserPO revisionEntity = null;
    try {
      revisionEntity = historyDao.getEntityAtPreviousRevision(BlossomUserPO.class,
          ((BlossomAssociationGroupUserPO) entity).getGroupId(), revision.getId());

    } catch (Exception e) {
      logger.warn("Can't fetch revision entry for entity type " + BlossomUserPO.class + " with id "
          + ((BlossomAssociationGroupUserPO) entity).getUserId(), e);
    }

    // If entity is not found when revised, we try to fetch the current state
    if (revisionEntity == null) {

    	revisionEntity = blossomUserRepository.findOne(((BlossomAssociationGroupUserPO) entity).getUserId());
    }

    return revisionEntity;
  }

  @Override
  protected <E extends BlossomAbstractEntity> Class<BlossomGroupPO> getSlaveEntityClass(BlossomRevisionEntity revision, E entity) {
    return BlossomGroupPO.class;
  }

  @Override
  protected <E extends BlossomAbstractEntity> BlossomGroupPO getSlaveEntity(BlossomRevisionEntity revision, E entity) {
    Preconditions.checkArgument(this.supports(entity.getClass()));
    BlossomGroupPO revisionEntity = null;
    try {
      revisionEntity = historyDao.getEntityAtPreviousRevision(BlossomGroupPO.class,
          ((BlossomAssociationGroupUserPO) entity).getGroupId(), revision.getId());

    } catch (Exception e) {
      logger.warn("Can't fetch revision entry for entity type " + BlossomGroupPO.class + " with id "
          + ((BlossomAssociationGroupUserPO) entity).getGroupId(), e);
    }
    // If entity is not found when revised, we try to fetch the current state
    if (revisionEntity == null) {
    	revisionEntity = blossomGroupRepository.findOne(((BlossomAssociationGroupUserPO) entity).getGroupId());
    }

    return revisionEntity;
  }

  public boolean supports(Class<? extends BlossomAbstractEntity> delimiter) {
    return BlossomAssociationGroupUserPO.class.isAssignableFrom(delimiter);
  }

}
