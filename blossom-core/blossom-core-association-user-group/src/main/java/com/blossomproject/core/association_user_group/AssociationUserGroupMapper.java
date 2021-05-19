package com.blossomproject.core.association_user_group;

import com.blossomproject.core.common.mapper.BlossomMapper;
import com.blossomproject.core.group.GroupMapper;
import com.blossomproject.core.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {UserMapper.class, GroupMapper.class})
public interface AssociationUserGroupMapper extends BlossomMapper<AssociationUserGroup, AssociationUserGroupDTO> {
}
