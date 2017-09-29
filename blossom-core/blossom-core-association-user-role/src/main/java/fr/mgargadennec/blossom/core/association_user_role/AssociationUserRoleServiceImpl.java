package fr.mgargadennec.blossom.core.association_user_role;

import fr.mgargadennec.blossom.core.common.service.GenericAssociationServiceImpl;
import fr.mgargadennec.blossom.core.role.Role;
import fr.mgargadennec.blossom.core.role.RoleDTO;
import fr.mgargadennec.blossom.core.role.RoleDTOMapper;
import fr.mgargadennec.blossom.core.user.User;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserDTOMapper;
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
