package fr.mgargadennec.blossom.core.association.user.role.support.history.builder.impl;

import com.google.common.base.Preconditions;

import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.history.builder.BlossomAssociationEntityAbstractHistoryBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.right.model.BlossomRightPO;
import fr.mgargadennec.blossom.core.role.model.BlossomRolePO;
import fr.mgargadennec.blossom.core.role.repository.BlossomRoleRepository;

public class BlossomAssociationRoleRightHistoryBuilderImpl extends
    BlossomAssociationEntityAbstractHistoryBuilder<BlossomRightPO, BlossomRolePO> {

  private IBlossomHistoryDAO historyDao;
  private BlossomRoleRepository roleRepository;

  public BlossomAssociationRoleRightHistoryBuilderImpl(IBlossomEntityDiffBuilder diffBuilder,
      BlossomRoleRepository roleRepository, IBlossomHistoryDAO historyDao) {
    super(diffBuilder);
    this.historyDao = historyDao;
    this.roleRepository = roleRepository;
  }

  @Override
  protected <E extends BlossomAbstractEntity> Class<BlossomRightPO> getMasterEntityClass(BlossomRevisionEntity revision, E entity) {
    return BlossomRightPO.class;
  }

  @Override
  protected <E extends BlossomAbstractEntity> BlossomRightPO getMasterEntity(BlossomRevisionEntity revision, E entity) {
    return (BlossomRightPO) entity;
  }

  @Override
  protected <E extends BlossomAbstractEntity> Class<BlossomRolePO> getSlaveEntityClass(BlossomRevisionEntity revision, E entity) {
    return BlossomRolePO.class;
  }

  @Override
  protected <E extends BlossomAbstractEntity> BlossomRolePO getSlaveEntity(BlossomRevisionEntity revision, E entity) {
    Preconditions.checkArgument(this.supports(entity.getClass()));

    BlossomRolePO role = historyDao.getEntityAtPreviousRevision(BlossomRolePO.class, ((BlossomRightPO) entity).getRoleId(),
        revision.getId());
    if (role == null) {
      role = roleRepository.findOne(((BlossomRightPO) entity).getRoleId());
    }

    return role;
  }

  public boolean supports(Class<? extends BlossomAbstractEntity> delimiter) {
    return BlossomRightPO.class.isAssignableFrom(delimiter);
  }
}
