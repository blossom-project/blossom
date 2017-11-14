package fr.blossom.core.association_user_role;

import fr.blossom.core.common.dao.AssociationDao;
import fr.blossom.core.role.Role;
import fr.blossom.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserRoleDao extends AssociationDao<User, Role, AssociationUserRole> {

}
