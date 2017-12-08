package fr.blossom.core.association_user_group;

import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.service.GenericAssociationServiceImpl;
import fr.blossom.core.group.Group;
import fr.blossom.core.group.GroupDTO;
import fr.blossom.core.group.GroupDTOMapper;
import fr.blossom.core.user.User;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserDTOMapper;
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
