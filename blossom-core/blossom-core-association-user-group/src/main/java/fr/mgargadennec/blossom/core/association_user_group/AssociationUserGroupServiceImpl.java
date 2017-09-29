package fr.mgargadennec.blossom.core.association_user_group;

import fr.mgargadennec.blossom.core.common.service.GenericAssociationServiceImpl;
import fr.mgargadennec.blossom.core.group.Group;
import fr.mgargadennec.blossom.core.group.GroupDTO;
import fr.mgargadennec.blossom.core.group.GroupDTOMapper;
import fr.mgargadennec.blossom.core.user.User;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserDTOMapper;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserGroupServiceImpl extends GenericAssociationServiceImpl<UserDTO, GroupDTO, AssociationUserGroupDTO, User, Group, AssociationUserGroup> implements AssociationUserGroupService {

    public AssociationUserGroupServiceImpl(AssociationUserGroupDao dao, AssociationUserGroupDTOMapper mapper, UserDTOMapper aMapper, GroupDTOMapper bMapper,
      ApplicationEventPublisher eventPublisher) {
        super(dao, mapper, aMapper, bMapper,eventPublisher);
    }
}
