package com.blossom_project.core.association_user_role;

import com.blossom_project.core.common.dao.GenericAssociationDaoImpl;
import com.blossom_project.core.role.Role;
import com.blossom_project.core.user.User;

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
