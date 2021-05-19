package com.blossomproject.core.group;

import com.blossomproject.core.common.mapper.AbstractBlossomDTOMapper;
import org.mapstruct.factory.Mappers;

public class GroupDTOMapper extends AbstractBlossomDTOMapper<Group, GroupDTO> {

    public GroupDTOMapper() {
        super(Mappers.getMapper(GroupMapper.class));
    }
}
