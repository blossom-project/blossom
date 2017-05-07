package fr.mgargadennec.blossom.core.association_user_role;

import fr.mgargadennec.blossom.core.common.service.AssociationService;
import fr.mgargadennec.blossom.core.role.RoleDTO;
import fr.mgargadennec.blossom.core.user.UserDTO;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserRoleService extends AssociationService<UserDTO, RoleDTO, AssociationUserRoleDTO> {

}
