package com.blossomproject.core.group;

import com.blossomproject.core.common.mapper.BlossomEntityMapper;
import com.blossomproject.core.common.mapper.BlossomEntityMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BlossomEntityMapperConfig.class)
public interface GroupEntityMapper extends BlossomEntityMapper<Group> {
}
