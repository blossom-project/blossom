package fr.mgargadennec.blossom.core.group.repository;

import org.springframework.stereotype.Repository;

import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;
import fr.mgargadennec.blossom.core.group.model.BlossomGroupPO;

/**
 * Repository de l'entite {@link BlossomGroupPO}
 *
 * @author Mael Gargadennec
 *
 */
@Repository
public interface BlossomGroupRepository extends BlossomJpaRepository<BlossomGroupPO> {

}
