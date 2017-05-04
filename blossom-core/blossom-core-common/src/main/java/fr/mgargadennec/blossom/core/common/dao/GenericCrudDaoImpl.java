package fr.mgargadennec.blossom.core.common.dao;

import com.querydsl.core.types.Predicate;
import fr.mgargadennec.blossom.core.common.PredicateProvider;
import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;
import fr.mgargadennec.blossom.core.common.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class GenericCrudDaoImpl<ENTITY extends AbstractEntity> extends GenericReadOnlyDaoImpl<ENTITY> implements CrudDao<ENTITY> {

  protected GenericCrudDaoImpl(CrudRepository<ENTITY, Long> repository, PredicateProvider predicateProvider) {
    super(repository, predicateProvider);
  }

  @Override
  @Transactional
  public ENTITY create(ENTITY toCreate) {
    return this.repository.save(toCreate);
  }

  @Override
  @Transactional
  public void delete(ENTITY toDelete) {
    this.repository.delete(toDelete.getId());
  }

  @Override
  @Transactional
  public ENTITY update(long id, ENTITY toUpdate) {
    ENTITY entity = this.repository.findOne(id);
    ENTITY modifiedEntity = toUpdate;
    entity = this.updateEntity(entity, modifiedEntity);

    return this.repository.save(entity);
  }

  @Override
  @Transactional
  public List<ENTITY> create(Collection<ENTITY> toCreates) {
    return this.repository.save(toCreates);
  }

  @Override
  @Transactional
  public List<ENTITY> update(Map<Long, ENTITY> toUpdates) {
    Predicate predicate = predicateProvider.getPredicate();
    List<ENTITY> entities = this.repository.findAll(predicate)
      .stream()
      .filter(dbEntity -> toUpdates.containsKey(dbEntity.getId()))
      .map(dbEntity -> this.updateEntity(dbEntity, toUpdates.get(dbEntity.getId())))
      .collect(Collectors.toList());
    return this.repository.save(entities);
  }

  protected abstract ENTITY updateEntity(ENTITY originalEntity, ENTITY modifiedEntity);

}
