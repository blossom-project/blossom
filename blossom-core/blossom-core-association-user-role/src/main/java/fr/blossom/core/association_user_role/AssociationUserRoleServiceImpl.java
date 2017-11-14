package fr.blossom.core.association_user_role;

import fr.blossom.core.common.service.GenericAssociationServiceImpl;
import fr.blossom.core.role.Role;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleDTOMapper;
import fr.blossom.core.user.User;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserDTOMapper;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserRoleServiceImpl extends
  GenericAssociationServiceImpl<UserDTO, RoleDTO, AssociationUserRoleDTO, User, Role, AssociationUserRole> implements
  AssociationUserRoleService {

  public AssociationUserRoleServiceImpl(AssociationUserRoleDao dao,
    AssociationUserRoleDTOMapper mapper, UserDTOMapper aMapper, RoleDTOMapper bMapper,
    ApplicationEventPublisher eventPublisher) {
    super(dao, mapper, aMapper, bMapper, eventPublisher);
  }
}
