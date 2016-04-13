package fr.mgargadennec.blossom.core.association.group.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.association.group.entity.model.BlossomAssociationGroupEntityPO;
import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;

/**
 * Repository de l'entite {@link BlossomAssociationGroupUserPO}
 *
 * @author Jeremie Treillard
 *
 */
@Repository
public interface BlossomAssociationGroupEntityRepository extends BlossomJpaRepository<BlossomAssociationGroupEntityPO> {

  Page<BlossomAssociationGroupEntityPO> findAllByEntityIdAndEntityType(Pageable pageable, Long entityId, String entityType);

  Page<BlossomAssociationGroupEntityPO> findAllByGroupId(Pageable pageable, Long groupId);

  Page<BlossomAssociationGroupEntityPO> findByGroupIdAndEntityIdAndEntityType(Pageable pageable, Long groupId,
      Long entityId, String entityType);

  Page<BlossomAssociationGroupEntityPO> findAllByEntityType(Pageable pageable, String entityType);

  @Transactional
  void deleteByGroupIdAndEntityIdAndEntityType(Long groupId, Long entityId, String entityType);

  @Transactional
  void deleteByEntityIdAndEntityType(Long entityId, String entityType);

  Page<BlossomAssociationGroupEntityPO> findAllByEntityTypeAndGroupId(Pageable pageable, String resourceType, Long groupId);

  Page<BlossomAssociationGroupEntityPO> findAllByEntityIdAndEntityTypeAndGroupId(Pageable pageable, Long entityId,
      String resourceType, Long groupId);

  void deleteByGroupId(Long groupId);

}
