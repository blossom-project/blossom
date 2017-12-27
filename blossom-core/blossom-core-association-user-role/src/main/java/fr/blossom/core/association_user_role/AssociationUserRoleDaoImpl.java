package fr.blossom.core.association_user_role;

import fr.blossom.core.common.dao.GenericAssociationDaoImpl;
import fr.blossom.core.role.Role;
import fr.blossom.core.user.User;

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
