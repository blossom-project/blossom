package fr.mgargadennec.blossom.core.role.repository;

import org.springframework.stereotype.Repository;

import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;
import fr.mgargadennec.blossom.core.role.model.BlossomRolePO;

@Repository
public interface BlossomRoleRepository extends BlossomJpaRepository<BlossomRolePO> {

}
