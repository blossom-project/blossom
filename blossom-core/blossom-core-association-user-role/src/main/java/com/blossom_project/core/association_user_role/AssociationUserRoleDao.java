package com.blossom_project.core.association_user_role;

import com.blossom_project.core.common.dao.AssociationDao;
import com.blossom_project.core.role.Role;
import com.blossom_project.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserRoleDao extends AssociationDao<User, Role, AssociationUserRole> {

}
