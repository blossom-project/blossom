package com.blossomproject.core.association_user_role;

import com.blossomproject.core.common.dao.AssociationDao;
import com.blossomproject.core.role.Role;
import com.blossomproject.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserRoleDao extends AssociationDao<User, Role, AssociationUserRole> {

}
