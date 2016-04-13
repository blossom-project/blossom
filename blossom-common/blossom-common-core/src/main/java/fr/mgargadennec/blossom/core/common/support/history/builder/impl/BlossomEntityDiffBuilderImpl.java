package fr.mgargadennec.blossom.core.common.support.history.builder.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryChangeDTO;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryTechnicalException;

public class BlossomEntityDiffBuilderImpl implements IBlossomEntityDiffBuilder {

  private Javers javers;

  public BlossomEntityDiffBuilderImpl(Javers javers) {
    this.javers = javers;
  }

  public List<BlossomHistoryChangeDTO> computeDiff(BlossomAbstractEntity beforeEntity, BlossomAbstractEntity afterEntity) {
    if (!beforeEntity.getClass().equals(afterEntity.getClass())) {
      throw new IllegalArgumentException("Both entities should have the same type to compute differences.");
    }

    Diff diff = javers.compare(beforeEntity, afterEntity);
    List<ValueChange> changeList = diff.getChangesByType(ValueChange.class);
    return createHistoryChangeDTOlistFromValueChangeList(changeList);
  }

  public List<BlossomHistoryChangeDTO> computeDiffWithoutBefore(BlossomAbstractEntity afterEntity)
      throws BlossomHistoryTechnicalException {
    Class<? extends BlossomAbstractEntity> entityClazz = afterEntity.getClass();

    BlossomAbstractEntity emptyEntity = newEmptyEntity(entityClazz);

    emptyEntity.setId(afterEntity.getId());
    emptyEntity.setUserCreation(afterEntity.getUserCreation());

    Diff diff = javers.compare(emptyEntity, afterEntity);
    List<ValueChange> changeList = diff.getChangesByType(ValueChange.class);
    return createHistoryChangeDTOlistFromValueChangeList(changeList);
  }

  public List<BlossomHistoryChangeDTO> computeDiffWithoutAfter(BlossomAbstractEntity beforeEntity)
      throws BlossomHistoryTechnicalException {
    Class<? extends BlossomAbstractEntity> entityClazz = beforeEntity.getClass();

    BlossomAbstractEntity emptyEntity = newEmptyEntity(entityClazz);

    emptyEntity.setId(beforeEntity.getId());
    emptyEntity.setUserCreation(beforeEntity.getUserCreation());

    Diff diff = javers.compare(beforeEntity, emptyEntity);
    List<ValueChange> changeList = diff.getChangesByType(ValueChange.class);
    return createHistoryChangeDTOlistFromValueChangeList(changeList);
  }

  private BlossomAbstractEntity newEmptyEntity(Class<? extends BlossomAbstractEntity> entityClazz)
      throws BlossomHistoryTechnicalException {
    BlossomAbstractEntity emptyEntity;

    try {
      Constructor<? extends BlossomAbstractEntity> entityCtor = entityClazz.getConstructor(null);
      emptyEntity = entityCtor.newInstance(null);

      return emptyEntity;
    } catch (Exception e) {
      throw new BlossomHistoryTechnicalException("Unable to construct an empty entity for " + entityClazz.getName(), e);
    }
  }

  private List<BlossomHistoryChangeDTO> createHistoryChangeDTOlistFromValueChangeList(List<ValueChange> changeList) {
    List<BlossomHistoryChangeDTO> historyChangeDtoList = new ArrayList<BlossomHistoryChangeDTO>();

    for (ValueChange change : changeList) {
      historyChangeDtoList.add(createHistoryChangeDTOfromValueChange(change));
    }

    return historyChangeDtoList;
  }

  private BlossomHistoryChangeDTO createHistoryChangeDTOfromValueChange(ValueChange change) {
	  BlossomHistoryChangeDTO historyChangeDto = new BlossomHistoryChangeDTO();

    historyChangeDto.setProperty(change.getPropertyName());
    if (change.getLeft() != null) {
      historyChangeDto.setLeft(change.getLeft().toString());
    } else {
      historyChangeDto.setLeft(null);
    }
    if (change.getRight() != null) {
      historyChangeDto.setRight(change.getRight().toString());
    } else {
      historyChangeDto.setRight(null);
    }

    return historyChangeDto;
  }

}
