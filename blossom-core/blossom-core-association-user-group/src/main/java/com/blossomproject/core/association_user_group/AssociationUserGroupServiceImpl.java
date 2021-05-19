package com.blossomproject.core.association_user_group;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.service.BlossomGenericAssociationServiceImpl;
import com.blossomproject.core.group.Group;
import com.blossomproject.core.group.GroupDTO;
import com.blossomproject.core.group.GroupDTOMapper;
import com.blossomproject.core.user.User;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserDTOMapper;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserGroupServiceImpl extends
        BlossomGenericAssociationServiceImpl<UserDTO, GroupDTO, AssociationUserGroupDTO, User, Group, AssociationUserGroup> implements
        AssociationUserGroupService {

    public AssociationUserGroupServiceImpl(AssociationUserGroupDao dao,
            AssociationUserGroupDTOMapper mapper, UserDTOMapper aMapper, GroupDTOMapper bMapper,
            ApplicationEventPublisher eventPublisher) {
        super(dao, mapper, aMapper, bMapper, eventPublisher);
    }

    @Override
    public boolean supports(Class<? extends AbstractDTO> delimiter) {
        return delimiter.isAssignableFrom(UserDTO.class) || delimiter.isAssignableFrom(GroupDTO.class);
    }
}
