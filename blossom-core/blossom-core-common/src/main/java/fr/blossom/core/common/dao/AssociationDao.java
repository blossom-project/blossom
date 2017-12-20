package fr.blossom.core.common.dao;

import fr.blossom.core.common.entity.AbstractAssociationEntity;
import fr.blossom.core.common.entity.AbstractEntity;
import java.util.List;

/**
 * Generic DAO interface whose purpose is to manage two {@code AbstractEntity}'s N-N relationships.
 *
 * @param <A> the first {@code AbstractEntity} type
 * @param <B> the second {@code AbstractEntity} type
 * @param <ASSOCIATION> the {@code AbstractAssociationEntity} type.
 *
 * @see AbstractAssociationEntity : association entity class
 * @see GenericAssociationDaoImpl : the default implementation
 *
 * @author Maël Gargadennec
 */
public interface AssociationDao<A extends AbstractEntity, B extends AbstractEntity, ASSOCIATION extends AbstractAssociationEntity<A, B>> {

  /**
   * Creates a new association between two {@code AbstractEntity}.<br/>
   * If the association already exists, retrieves it.
   *
   * @param a the first {@code AbstractEntity}. If null, throws an {@code IllegalArgumentException}.
   * @param b the second {@code AbstractEntity}. If null, throws an {@code IllegalArgumentException}.
   * @return the created or already existing association
   */
  ASSOCIATION associate(A a, B b);

  /**
   * Removes and existing association between two {@code AbstractEntity}.<br/>
   * If the association does not exist, this method does nothing.
   *
   * @param a the first {@code AbstractEntity}. If null, throws an {@code IllegalArgumentException}.
   * @param b the second {@code AbstractEntity}. If null, throws an {@code IllegalArgumentException}.
   */
  void dissociate(A a, B b);

  /**
   * Retrieve a {@code List} of all {@code A}/{@code B} associations for a given {@code B} element
   *
   * @param b an {@code AbstractEntity} of parametrized type {@code B}
   * @return the list of all available associations to {@code A} elements
   */
  List<ASSOCIATION> getAllA(B b);

  /**
   * Retrieve a {@code List} of all {@code A}/{@code B} associations for a given {@code A} element
   *
   * @param a an {@code AbstractEntity} of parametrized type {@code A}
   * @return the list of all available associations to {@code B} elements
   */
  List<ASSOCIATION> getAllB(A a);

  /**
   * Get an {@code A}/{@code B} association by its identifier.
   *
   * @param id the association id
   * @return the association, or {@literal null}  if it doesn't exist
   */
  ASSOCIATION getOne(long id);

  /**
   * Get an {@code A}/{@code B} association by its associated entities.
   *
   * @param a an {@code AbstractEntity} of parametrized type {@code A}
   * @param b an {@code AbstractEntity} of parametrized type {@code B}
   * @return the association, or {@literal null} if not found
   */
  ASSOCIATION getOne(A a, B b);
}
