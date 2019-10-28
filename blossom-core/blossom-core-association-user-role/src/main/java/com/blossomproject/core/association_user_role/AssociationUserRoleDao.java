package com.blossomproject.core.association_user_role;

import com.blossomproject.core.common.dao.BlossomAssociationDao;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.role.Role;
import com.blossomproject.core.user.User;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserRoleDao extends BlossomAssociationDao<User, Role, AssociationUserRole> {

    boolean getUserExistsByPrivilege(List<Privilege> privilege);

}
