package fr.blossom.core.common.dao;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import fr.blossom.core.common.entity.AbstractEntity;
import fr.blossom.core.common.repository.CrudRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.util.Assert;

/**
 * Default abstract implementation of {@link ReadOnlyDao}.
 *
 * @param <ENTITY> the managed {@link AbstractEntity}
 * @author Maël Gargadennec
 */
public abstract class GenericReadOnlyDaoImpl<ENTITY extends AbstractEntity> implements
  ReadOnlyDao<ENTITY> {

  protected final CrudRepository<ENTITY> repository;

  private Querydsl querydsl;
  private EntityManager entityManager;

  protected TypeToken<ENTITY> type = new TypeToken<ENTITY>(getClass()) {
  };

  GenericReadOnlyDaoImpl(CrudRepository<ENTITY> repository) {
    Preconditions.checkNotNull(repository);
    this.repository = repository;
  }

  @PostConstruct
  public void validate() {
    Assert.notNull(entityManager, "EntityManager must not be null!");
    Assert.notNull(querydsl, "Querydsl must not be null!");
  }

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    assert entityManager != null;

    PathBuilder<?> builder = new PathBuilderFactory().create(type.getRawType());
    this.querydsl = new Querydsl(entityManager, builder);
    this.entityManager = entityManager;
  }

  @Override
  @Cacheable(key = "#a0+''")
  public ENTITY getOne(long id) {
    return this.repository.findOne(id);
  }

  @Override
  @Cacheable(key = "'all'")
  public List<ENTITY> getAll() {
    return this.repository.findAll();
  }

  @Override
  @Cacheable
  public List<ENTITY> getAll(List<Long> ids) {
    Preconditions.checkArgument(!ids.isEmpty());
    return this.repository.findAll(ids);
  }

  @Override
  @Cacheable
  public Page<ENTITY> getAll(Pageable pageable) {
    Preconditions.checkArgument(pageable != null);
    return repository.findAll(pageable);
  }

  /**
   * Returns a fresh {@link JPQLQuery}.
   *
   * @param paths must not be {@literal null}.
   * @return the Querydsl {@link JPQLQuery}.
   */
  protected JPQLQuery<Object> from(EntityPath<?>... paths) {
    return querydsl.createQuery(paths);
  }

  /**
   * Returns a {@link JPQLQuery} for the given {@link EntityPath}.
   *
   * @param path must not be {@literal null}.
   */
  protected <T> JPQLQuery<T> from(EntityPath<T> path) {
    return querydsl.createQuery(path).select(path);
  }

  /**
   * Returns the underlying Querydsl helper instance.
   */
  protected Querydsl getQuerydsl() {
    return this.querydsl;
  }

}
