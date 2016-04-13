package fr.mgargadennec.blossom.core.user.repository;

import org.springframework.stereotype.Repository;

import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;
import fr.mgargadennec.blossom.core.user.model.BlossomUserPO;

/**
 * Repository de l'entite {@link BlossomUserPO}
 *
 * @author Mael Gargadennec
 *
 */
@Repository
public interface BlossomUserRepository extends BlossomJpaRepository<BlossomUserPO> {

  BlossomUserPO findByEmail(String email);

  BlossomUserPO findByLogin(String login);

}
