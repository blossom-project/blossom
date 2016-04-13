package fr.mgargadennec.blossom.core.association.group.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.association.group.user.model.BlossomAssociationGroupUserPO;
import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;

/**
 * Repository de l'entite {@link BlossomAssociationGroupUserPO}
 *
 * @author Jeremie Treillard
 *
 */
@Repository
public interface BlossomAssociationGroupUserRepository extends BlossomJpaRepository<BlossomAssociationGroupUserPO> {

  Page<BlossomAssociationGroupUserPO> findAllByUserId(Pageable pageable, Long userId);

  Page<BlossomAssociationGroupUserPO> findAllByGroupId(Pageable pageable, Long groupId);

  Page<BlossomAssociationGroupUserPO> findAllByGroupIdAndUserId(Pageable pageable, Long groupId, Long userId);

  @Transactional
  void deleteByGroupId(Long groupId);

  @Transactional
  void deleteByUserId(Long userId);

}
