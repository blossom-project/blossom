package fr.mgargadennec.blossom.core.common.support.history.builder;

import java.util.List;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryChangeDTO;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryTechnicalException;

public interface IBlossomEntityDiffBuilder {

  List<BlossomHistoryChangeDTO> computeDiff(BlossomAbstractEntity beforeEntity, BlossomAbstractEntity afterEntity)
      throws BlossomHistoryTechnicalException;

  List<BlossomHistoryChangeDTO> computeDiffWithoutBefore(BlossomAbstractEntity afterEntity) throws BlossomHistoryTechnicalException;
  
  List<BlossomHistoryChangeDTO> computeDiffWithoutAfter(BlossomAbstractEntity beforeEntity) throws BlossomHistoryTechnicalException;

}
