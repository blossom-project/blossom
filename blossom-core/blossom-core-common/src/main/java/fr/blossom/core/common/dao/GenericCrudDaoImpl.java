package fr.blossom.core.common.dao;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fr.blossom.core.common.entity.AbstractEntity;
import fr.blossom.core.common.repository.CrudRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;


/**
 * Default abstract implementation of {@link CrudDao}.
 *
 * @param <ENTITY> the managed {@link AbstractEntity}
 * @author Maël Gargadennec
 */
public abstract class GenericCrudDaoImpl<ENTITY extends AbstractEntity> extends
  GenericReadOnlyDaoImpl<ENTITY> implements CrudDao<ENTITY> {

  protected GenericCrudDaoImpl(CrudRepository<ENTITY> repository) {
    super(repository);
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public ENTITY create(ENTITY toCreate) {
    return this.repository.save(toCreate);
  }

  @Override
  @Transactional
  @CacheEvict(key = "#a0.id+''")
  public void delete(ENTITY toDelete) {
    Preconditions.checkArgument(toDelete != null);
    Preconditions.checkArgument(toDelete.getId() != null);
    this.repository.delete(toDelete.getId());
  }

  @Override
  @Transactional
  @CachePut(key = "#a0+''")
  public ENTITY update(long id, ENTITY toUpdate) {
    Preconditions.checkArgument(toUpdate != null);
    ENTITY entity = this.repository.findOne(id);
    Preconditions.checkArgument(entity != null);
    Preconditions.checkArgument(entity.getId() != null);
    Preconditions.checkArgument(entity.getId().equals(toUpdate.getId()));

    ENTITY modifiedEntity = toUpdate;
    entity = this.updateEntity(entity, modifiedEntity);
    return this.repository.save(entity);
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public List<ENTITY> create(Collection<ENTITY> toCreates) {
    Preconditions.checkArgument(toCreates != null);
    if(toCreates.isEmpty()){
      return Lists.newArrayList();
    }
    return this.repository.save(toCreates);
  }

  @Override
  @Transactional
  public List<ENTITY> update(Map<Long, ENTITY> toUpdates) {
    Preconditions.checkArgument(toUpdates != null);
    if(toUpdates.isEmpty()){
      return Lists.newArrayList();
    }
    List<ENTITY> entities = this.repository.findAll(toUpdates.keySet())
      .stream()
      .filter(dbEntity -> toUpdates.containsKey(dbEntity.getId()))
      .map(dbEntity -> this.updateEntity(dbEntity, toUpdates.get(dbEntity.getId())))
      .collect(Collectors.toList());
    return this.repository.save(entities);
  }

  protected abstract ENTITY updateEntity(ENTITY originalEntity, ENTITY modifiedEntity);

}
