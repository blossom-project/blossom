package fr.mgargadennec.blossom.core.user.repository;

import org.springframework.stereotype.Repository;

import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;
import fr.mgargadennec.blossom.core.user.model.BlossomUserLoginAttemptsPO;

/**
 * Repository de l'entite {@link BlossomUserLoginAttemptsPO}
 */
@Repository
public interface BlossomUserLoginAttemptsRepository extends BlossomJpaRepository<BlossomUserLoginAttemptsPO> {

  /**
   * Retrouve une tentative d identification d un login donne
   *
   * @param login le login
   * @return la tentative de login (null s'il n'y en a pas eu)
   */
  BlossomUserLoginAttemptsPO findByLogin(String login);

  /**
   * Supprime une tentative d identification d un login donne
   *
   * @param login le login
   */
  void deleteByLogin(String login);

}
