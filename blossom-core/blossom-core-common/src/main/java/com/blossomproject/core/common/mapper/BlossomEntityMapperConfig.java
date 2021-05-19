package com.blossomproject.core.common.mapper;

import com.blossomproject.core.common.entity.AbstractEntity;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

@MapperConfig
public interface BlossomEntityMapperConfig {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    AbstractEntity merge(AbstractEntity modifiedEntity);

}
