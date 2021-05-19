package com.blossomproject.core.user;

import com.blossomproject.core.common.mapper.BlossomMapper;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper extends BlossomMapper<User,UserDTO> {
}
