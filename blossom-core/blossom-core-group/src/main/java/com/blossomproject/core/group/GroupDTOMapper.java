package com.blossomproject.core.group;

import com.blossomproject.core.common.mapper.AbstractDTOMapper;

public class GroupDTOMapper extends AbstractDTOMapper<Group, GroupDTO> {

    @Override
    public GroupDTO mapEntity(Group entity) {
        if (entity == null) {
            return null;
        }

        GroupDTO dto = new GroupDTO();
        mapEntityCommonFields(dto, entity);
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }

    @Override
    public Group mapDto(GroupDTO dto) {
        if (dto == null) {
            return null;
        }

        Group entity = new Group();
        mapDtoCommonFields(entity, dto);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
