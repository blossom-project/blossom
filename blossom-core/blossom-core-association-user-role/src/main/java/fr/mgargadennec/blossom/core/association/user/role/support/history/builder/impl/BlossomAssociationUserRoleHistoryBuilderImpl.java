package fr.mgargadennec.blossom.core.association.user.role.support.history.builder.impl;

import com.google.common.base.Preconditions;

import fr.mgargadennec.blossom.core.association.user.role.model.BlossomAssociationUserRolePO;
import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.history.builder.BlossomAssociationEntityAbstractHistoryBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.role.model.BlossomRolePO;
import fr.mgargadennec.blossom.core.user.model.BlossomUserPO;

public class BlossomAssociationUserRoleHistoryBuilderImpl extends
    BlossomAssociationEntityAbstractHistoryBuilder<BlossomRolePO, BlossomUserPO> {

  IBlossomHistoryDAO historyDao;

  public BlossomAssociationUserRoleHistoryBuilderImpl(IBlossomEntityDiffBuilder diffBuilder, IBlossomHistoryDAO historyDao) {
    super(diffBuilder);
    this.historyDao = historyDao;
  }

  @Override
  protected <E extends BlossomAbstractEntity> Class<BlossomRolePO> getMasterEntityClass(BlossomRevisionEntity revision, E entity) {
    return BlossomRolePO.class;
  }

  @Override
  protected <E extends BlossomAbstractEntity> BlossomRolePO getMasterEntity(BlossomRevisionEntity revision, E entity) {
    Preconditions.checkArgument(this.supports(entity.getClass()));
    return historyDao.getEntityAtPreviousRevision(BlossomRolePO.class, ((BlossomAssociationUserRolePO) entity).getRoleId(),
        revision.getId());
  }

  @Override
  protected <E extends BlossomAbstractEntity> Class<BlossomUserPO> getSlaveEntityClass(BlossomRevisionEntity revision, E entity) {
    return BlossomUserPO.class;
  }

  @Override
  protected <E extends BlossomAbstractEntity> BlossomUserPO getSlaveEntity(BlossomRevisionEntity revision, E entity) {
    Preconditions.checkArgument(this.supports(entity.getClass()));
    return historyDao.getEntityAtPreviousRevision(BlossomUserPO.class, ((BlossomAssociationUserRolePO) entity).getUserId(),
        revision.getId());
  }

  public boolean supports(Class<? extends BlossomAbstractEntity> delimiter) {
    return BlossomAssociationUserRolePO.class.isAssignableFrom(delimiter);
  }
}
