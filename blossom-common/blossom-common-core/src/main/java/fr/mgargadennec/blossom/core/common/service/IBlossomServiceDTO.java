package fr.mgargadennec.blossom.core.common.service;

import java.util.Date;

import fr.mgargadennec.blossom.core.common.model.common.BlossomIdentifiable;


public interface IBlossomServiceDTO extends BlossomIdentifiable<Long> {

  /**
   * Retourne l'identifiant du service
   */
  Long getId();

  /**
   * Retourne la date de creation du service
   *
   * @return la date de creation du service
   */
  Date getDateCreation();

  /**
   * Retourne la date de derniere modification du service
   *
   * @return la date de derniere modification du service
   */
  Date getDateModification();

  /**
   * Retourne le login du createur du service
   *
   * @return le login du createur du service
   */
  String getUserCreation();

  /**
   * Retourne le login du dernier modificateur du service
   *
   * @return le login du dernier modificateur du service
   */
  String getUserModification();

}