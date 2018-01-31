package fr.blossom.core.common.dao;

import com.google.common.base.Preconditions;
import fr.blossom.core.common.entity.AbstractAssociationEntity;
import fr.blossom.core.common.entity.AbstractEntity;
import fr.blossom.core.common.repository.AssociationRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


/**
 * Default abstract implementation of {@link AssociationDao}.
 *
 * @param <A> the first {@code AbstractEntity} type
 * @param <B> the second {@code AbstractEntity} type
 * @param <ASSOCIATION> the {@code AbstractAssociationEntity} type.
 * @author Maël Gargadennec
 */
public abstract class GenericAssociationDaoImpl<A extends AbstractEntity, B extends AbstractEntity, ASSOCIATION extends AbstractAssociationEntity<A, B>>
  implements AssociationDao<A, B, ASSOCIATION> {

  private final AssociationRepository<A, B, ASSOCIATION> repository;

  protected GenericAssociationDaoImpl(AssociationRepository<A, B, ASSOCIATION> repository) {
    Preconditions.checkNotNull(repository);
    this.repository = repository;
  }

  @Override
  @Transactional
  public ASSOCIATION associate(A a, B b) {
    Preconditions.checkArgument(a != null);
    Preconditions.checkArgument(b != null);

    ASSOCIATION existing = repository.findOneByAAndB(a, b);
    if (existing != null) {
      return existing;
    }
    ASSOCIATION association = this.create();
    association.setA(a);
    association.setB(b);

    return repository.save(association);
  }

  @Override
  @Transactional
  public void dissociate(A a, B b) {
    Preconditions.checkArgument(a != null);
    Preconditions.checkArgument(b != null);

    ASSOCIATION association = repository.findOneByAAndB(a, b);
    if (association != null) {
      this.repository.delete(association);
    }
  }

  @Override
  public List<ASSOCIATION> getAllA(B b) {
    Preconditions.checkArgument(b != null);
    return this.repository.findAllByB(b);
  }

  @Override
  public List<ASSOCIATION> getAllB(A a) {
    Preconditions.checkArgument(a != null);
    return this.repository.findAllByA(a);
  }

  @Override
  public ASSOCIATION getOne(long id) {
    return this.repository.findById(id).orElse(null);
  }

  @Override
  public ASSOCIATION getOne(A a, B b) {
    Preconditions.checkArgument(a != null);
    Preconditions.checkArgument(b != null);
    return this.repository.findOneByAAndB(a, b);
  }

  @Transactional
  protected abstract ASSOCIATION create();
}
