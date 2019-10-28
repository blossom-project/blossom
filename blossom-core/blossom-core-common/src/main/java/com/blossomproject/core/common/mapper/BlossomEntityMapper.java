package com.blossomproject.core.common.mapper;

import com.blossomproject.core.common.entity.AbstractEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.MappingTarget;

public interface BlossomEntityMapper<E extends AbstractEntity> {

    @InheritConfiguration
    E merge(@MappingTarget E originalEntity, E modifiedEntity);

}
