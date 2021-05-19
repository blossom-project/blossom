package com.blossomproject.core.association_user_role;

import com.blossomproject.core.common.mapper.BlossomAssociationEntityMapperConfig;
import com.blossomproject.core.common.mapper.BlossomEntityMapper;
import org.mapstruct.Mapper;

@Mapper(config = BlossomAssociationEntityMapperConfig.class)
public interface AssociationUserRoleEntityMapper extends BlossomEntityMapper<AssociationUserRole> {
}
