package com.blossomproject.core.user;

import com.blossomproject.core.common.mapper.AbstractBlossomDTOMapper;
import org.mapstruct.factory.Mappers;

public class UserDTOMapper extends AbstractBlossomDTOMapper<User, UserDTO> {

    public UserDTOMapper() {
        super(Mappers.getMapper(UserMapper.class));
    }

}
