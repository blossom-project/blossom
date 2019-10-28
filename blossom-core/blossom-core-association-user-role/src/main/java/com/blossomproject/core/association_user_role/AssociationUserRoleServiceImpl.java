package com.blossomproject.core.association_user_role;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.service.BlossomGenericAssociationServiceImpl;
import com.blossomproject.core.common.service.GenericAssociationServiceImpl;
import com.blossomproject.core.role.Role;
import com.blossomproject.core.role.RoleDTO;
import com.blossomproject.core.role.RoleDTOMapper;
import com.blossomproject.core.user.User;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserDTOMapper;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserRoleServiceImpl extends
        BlossomGenericAssociationServiceImpl<UserDTO, RoleDTO, AssociationUserRoleDTO, User, Role, AssociationUserRole> implements
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
