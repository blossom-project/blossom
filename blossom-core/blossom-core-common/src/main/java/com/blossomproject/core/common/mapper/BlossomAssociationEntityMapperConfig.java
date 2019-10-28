package com.blossomproject.core.common.mapper;

import com.blossomproject.core.common.entity.AbstractAssociationEntity;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

@MapperConfig
public interface BlossomAssociationEntityMapperConfig {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "creationUser", ignore = true)
    @Mapping(target = "a", ignore = true)
    @Mapping(target = "b", ignore = true)
    AbstractAssociationEntity merge(AbstractAssociationEntity modifiedEntity);

}
