package com.blossom_project.core.association_user_role;

import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.service.GenericAssociationServiceImpl;
import com.blossom_project.core.role.Role;
import com.blossom_project.core.role.RoleDTO;
import com.blossom_project.core.role.RoleDTOMapper;
import com.blossom_project.core.user.User;
import com.blossom_project.core.user.UserDTO;
import com.blossom_project.core.user.UserDTOMapper;
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

  @Override
  public boolean supports(Class<? extends AbstractDTO> delimiter) {
    return delimiter.isAssignableFrom(RoleDTO.class) || delimiter.isAssignableFrom(UserDTO.class);
  }
}
