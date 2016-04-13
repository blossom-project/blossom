package fr.mgargadennec.blossom.core.association.user.role.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.association.user.role.model.BlossomAssociationUserRolePO;
import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;

/**
 * Repository de l'entite {@link BlossomAssociationUserRolePO}
 *
 * @author Jeremie Treillard
 *
 */
@Repository
public interface BlossomAssociationUserRoleRepository extends BlossomJpaRepository<BlossomAssociationUserRolePO> {

  Page<BlossomAssociationUserRolePO> findAllByRoleId(Pageable pageable, Long roleId);

  Page<BlossomAssociationUserRolePO> findAllByUserId(Pageable pageable, Long userId);

  Page<BlossomAssociationUserRolePO> findAllByUserIdAndRoleId(Pageable pageable, Long userId, Long roleId);

  @Transactional
  void deleteByUserId(Long userId);

  @Transactional
  void deleteByRoleId(Long roleId);

}
