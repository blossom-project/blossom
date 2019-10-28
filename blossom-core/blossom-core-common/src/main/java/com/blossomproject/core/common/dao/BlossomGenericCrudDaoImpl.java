package com.blossomproject.core.common.dao;

import com.blossomproject.core.common.entity.AbstractEntity;
import com.blossomproject.core.common.mapper.BlossomEntityMapper;
import com.blossomproject.core.common.repository.CrudRepository;
import com.google.common.base.Preconditions;

public abstract class BlossomGenericCrudDaoImpl<ENTITY extends AbstractEntity> extends GenericCrudDaoImpl<ENTITY> {

    private final BlossomEntityMapper<ENTITY> mapper;

    protected BlossomGenericCrudDaoImpl(CrudRepository<ENTITY> repository, BlossomEntityMapper<ENTITY> mapper) {
        super(repository);
        this.mapper = mapper;
    }

    @Override
    protected ENTITY updateEntity(ENTITY originalEntity, ENTITY modifiedEntity) {
        return mapper.merge(originalEntity, modifiedEntity);
    }

    @Override
    public ENTITY update(long id, ENTITY toUpdate) {
        Preconditions.checkArgument(toUpdate != null);
        ENTITY entity = this.repository.findById(id).orElse(null);
        Preconditions.checkArgument(entity != null);

        return this.repository.save(this.updateEntity(entity, toUpdate));
    }
}
