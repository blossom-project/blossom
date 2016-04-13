package fr.mgargadennec.blossom.core.common.support.history.builder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryDTO;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryIncoherentDataException;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryIncompleteDataException;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryTechnicalException;

@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_HISTORY_BUILDER_REGISTRY)
public interface IBlossomHistoryBuilder extends Plugin<Class<? extends BlossomAbstractEntity>> {

  <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> buildDTOfromADD(BlossomRevisionEntity revision, E afterEntity)
      throws BlossomHistoryIncompleteDataException, BlossomHistoryTechnicalException;

  <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> buildDTOfromMOD(BlossomRevisionEntity revision, E beforeEntity,
      E afterEntity) throws BlossomHistoryIncompleteDataException, BlossomHistoryIncoherentDataException, BlossomHistoryTechnicalException;

  <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> buildDTOfromMOD(BlossomRevisionEntity revision, E afterEntity)
      throws BlossomHistoryIncompleteDataException, BlossomHistoryTechnicalException;

  <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> buildDTOfromDEL(BlossomRevisionEntity revision, E beforeEntity)
      throws BlossomHistoryIncompleteDataException, BlossomHistoryTechnicalException;

}
