package fr.blossom.core.common.dao;

import fr.blossom.core.common.entity.AbstractEntity;
import fr.blossom.core.common.repository.CrudRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericCrudDaoImpl<ENTITY extends AbstractEntity> extends GenericReadOnlyDaoImpl<ENTITY> implements CrudDao<ENTITY> {

    protected GenericCrudDaoImpl(CrudRepository<ENTITY> repository) {
        super(repository);
    }

    @Override
    @Transactional
    public ENTITY create(ENTITY toCreate) {
        return this.repository.save(toCreate);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#a0.id+''")
    public void delete(ENTITY toDelete) {
        this.repository.delete(toDelete.getId());
    }

    @Override
    @Transactional
    @CachePut(key = "#a0+''")
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
        List<ENTITY> entities = this.repository.findAll(toUpdates.keySet())
                .stream()
                .filter(dbEntity -> toUpdates.containsKey(dbEntity.getId()))
                .map(dbEntity -> this.updateEntity(dbEntity, toUpdates.get(dbEntity.getId())))
                .collect(Collectors.toList());
        return this.repository.save(entities);
    }

    protected abstract ENTITY updateEntity(ENTITY originalEntity, ENTITY modifiedEntity);

}
