package com.blossomproject.core.association_user_role;

import com.blossomproject.core.common.dao.GenericAssociationDaoImpl;
import com.blossomproject.core.role.Role;
import com.blossomproject.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserRoleDaoImpl extends GenericAssociationDaoImpl<User, Role, AssociationUserRole> implements AssociationUserRoleDao {
    public AssociationUserRoleDaoImpl(AssociationUserRoleRepository repository) {
        super(repository);
    }

    @Override
    protected AssociationUserRole create() {
        return new AssociationUserRole();
    }
}
