package com.blossom_project.core.association_user_group;

import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.service.GenericAssociationServiceImpl;
import com.blossom_project.core.group.Group;
import com.blossom_project.core.group.GroupDTO;
import com.blossom_project.core.group.GroupDTOMapper;
import com.blossom_project.core.user.User;
import com.blossom_project.core.user.UserDTO;
import com.blossom_project.core.user.UserDTOMapper;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserGroupServiceImpl extends
  GenericAssociationServiceImpl<UserDTO, GroupDTO, AssociationUserGroupDTO, User, Group, AssociationUserGroup> implements
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
