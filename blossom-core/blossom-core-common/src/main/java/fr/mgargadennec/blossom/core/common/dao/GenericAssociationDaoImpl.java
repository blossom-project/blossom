package fr.mgargadennec.blossom.core.common.dao;

import com.google.common.base.Preconditions;
import fr.mgargadennec.blossom.core.common.entity.AbstractAssociationEntity;
import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;
import fr.mgargadennec.blossom.core.common.repository.AssociationRepository;
import java.util.List;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheResolver = "blossomCacheResolver")
public abstract class GenericAssociationDaoImpl<A extends AbstractEntity, B extends AbstractEntity, ASSOCIATION extends AbstractAssociationEntity<A, B>>
  implements AssociationDao<A, B, ASSOCIATION> {

  private final AssociationRepository<A, B, ASSOCIATION> repository;

  protected GenericAssociationDaoImpl(AssociationRepository<A, B, ASSOCIATION> repository) {
    this.repository = repository;
  }

  @Override
//  @Caching(evict = {@CacheEvict(key = "'a_'+#a0.id"), @CacheEvict(key = "'b_'+#a1.id")})
  public ASSOCIATION associate(A a, B b) {
    Preconditions.checkNotNull(a);
    Preconditions.checkNotNull(b);
    if (repository.findOneByAAndB(a, b) != null) {
      throw new RuntimeException("Association already exists !");
    }
    ASSOCIATION association = this.create();
    association.setA(a);
    association.setB(b);

    return repository.save(association);
  }

  @Override
//  @Caching(evict = {@CacheEvict(key = "'a_'+#a0.id"), @CacheEvict(key = "'b_'+#a1.id")})
  public void dissociate(A a, B b) {
    Preconditions.checkNotNull(a);
    Preconditions.checkNotNull(b);

    ASSOCIATION association = repository.findOneByAAndB(a, b);
    if (repository.findOneByAAndB(a, b) == null) {
      throw new RuntimeException("Association does not exists !");
    }
    this.repository.delete(association);
  }

  @Override
//  @Cacheable(key = "'b_'+#a0.id")
  public List<ASSOCIATION> getAllA(B b) {
    return this.repository.findAllByB(b);
  }

  @Override
//  @Cacheable(key = "'a_'+#a0.id")
  public List<ASSOCIATION> getAllB(A a) {
    return this.repository.findAllByA(a);
  }

  @Override
//  @Cacheable(key = "''+#a0")
  public ASSOCIATION getOne(long id) {
    return this.repository.findOne(id);
  }

  @Override
  public ASSOCIATION getOne(A a, B b) {
    return this.repository.findOneByAAndB(a, b);
  }

  protected abstract ASSOCIATION create();
}
