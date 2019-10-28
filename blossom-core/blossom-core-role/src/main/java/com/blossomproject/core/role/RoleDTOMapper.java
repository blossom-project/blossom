package com.blossomproject.core.role;

import com.blossomproject.core.common.mapper.AbstractBlossomDTOMapper;
import org.mapstruct.factory.Mappers;

public class RoleDTOMapper extends AbstractBlossomDTOMapper<Role, RoleDTO> {

    public RoleDTOMapper() {
        super(Mappers.getMapper(RoleMapper.class));
    }
}
