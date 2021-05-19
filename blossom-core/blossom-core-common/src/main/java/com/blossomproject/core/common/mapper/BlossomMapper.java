package com.blossomproject.core.common.mapper;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.entity.AbstractEntity;

public interface BlossomMapper<E extends AbstractEntity, D extends AbstractDTO> {

    D toDto(E entity);

    E toEntity(D dto);

}
