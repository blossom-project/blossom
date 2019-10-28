package com.blossomproject.core.role;

import com.blossomproject.core.common.mapper.BlossomEntityMapper;
import com.blossomproject.core.common.mapper.BlossomEntityMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BlossomEntityMapperConfig.class)
public interface RoleEntityMapper extends BlossomEntityMapper<Role> {
}
