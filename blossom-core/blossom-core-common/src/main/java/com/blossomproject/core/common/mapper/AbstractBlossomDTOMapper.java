package com.blossomproject.core.common.mapper;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.entity.AbstractEntity;

public abstract class AbstractBlossomDTOMapper<E extends AbstractEntity, D extends AbstractDTO> extends AbstractDTOMapper<E, D> {

    protected final BlossomMapper<E, D> mapper;

    public AbstractBlossomDTOMapper(BlossomMapper<E, D> mapper) {
        this.mapper = mapper;
    }

    @Override
    public D mapEntity(E entity) {
        return mapper.toDto(entity);
    }

    @Override
    public E mapDto(D dto) {
        return mapper.toEntity(dto);
    }

}
