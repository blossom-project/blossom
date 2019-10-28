package com.blossomproject.core.user;

import com.blossomproject.core.common.mapper.BlossomEntityMapper;
import com.blossomproject.core.common.mapper.BlossomEntityMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BlossomEntityMapperConfig.class)
public interface UserEntityMapper extends BlossomEntityMapper<User> {
}
