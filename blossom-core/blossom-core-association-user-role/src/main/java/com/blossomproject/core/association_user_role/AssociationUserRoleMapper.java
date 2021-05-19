package com.blossomproject.core.association_user_role;

import com.blossomproject.core.common.mapper.BlossomMapper;
import com.blossomproject.core.role.RoleMapper;
import com.blossomproject.core.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {UserMapper.class, RoleMapper.class})
public interface AssociationUserRoleMapper extends BlossomMapper<AssociationUserRole, AssociationUserRoleDTO> {
}
