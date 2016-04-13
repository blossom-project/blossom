package fr.mgargadennec.blossom.core.association.group.entity.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.group.entity.process.dto.BlossomAssociationGroupEntityProcessDTO;
import fr.mgargadennec.blossom.core.association.group.entity.service.dto.BlossomAssociationGroupEntityServiceDTO;
import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericService;

public interface IBlossomAssociationGroupEntityService extends
IBlossomGenericService<BlossomAssociationGroupEntityProcessDTO, BlossomAssociationGroupEntityServiceDTO> {
  Page<BlossomAssociationGroupEntityServiceDTO> getAllByEntityIdAndEntityType(Pageable pageable, Long entityId,
      String resourceType);

  Page<BlossomAssociationGroupEntityServiceDTO> getAllByEntityType(Pageable pageable, String resourceType);

  Page<BlossomAssociationGroupEntityServiceDTO> getAllByEntityTypeAndGroupId(Pageable pageable, String resourceType,
      Long groupId);

  Page<BlossomAssociationGroupEntityServiceDTO> getAllByEntityIdAndEntityTypeAndGroupId(Pageable pageable, Long entityId,
      String resourceType, Long groupId);

  void deleteByGroupIdAndEntityIdAndEntityType(Long groupId, Long entityId, String entityType);

  void deleteByEntityIdAndEntityType(Long entityId, String entityType);

  void mergeAssociationsBetweenEntities(Long fromEntityId, String fromEntityType, Long toEntityId, String toEntityType);

}
