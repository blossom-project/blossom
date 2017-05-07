package fr.mgargadennec.blossom.core.association_user_role;

import fr.mgargadennec.blossom.core.common.dao.AssociationDao;
import fr.mgargadennec.blossom.core.role.Role;
import fr.mgargadennec.blossom.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserRoleDao extends AssociationDao<User, Role, AssociationUserRole> {

}
