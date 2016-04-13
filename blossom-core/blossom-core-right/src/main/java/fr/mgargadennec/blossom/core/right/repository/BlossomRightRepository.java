package fr.mgargadennec.blossom.core.right.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;
import fr.mgargadennec.blossom.core.right.model.BlossomRightPO;

/**
 * Repository de l'entite {@link BlossomRightPO}
 *
 * @author Jeremie Treillard
 *
 */
@Repository
public interface BlossomRightRepository extends BlossomJpaRepository<BlossomRightPO> {

  Page<BlossomRightPO> findByRoleId(Pageable pageable, Long roleId);

  void deleteByRoleId(Long roleId);

}
