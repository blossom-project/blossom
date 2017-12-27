package fr.blossom.core.association_user_group;

import fr.blossom.core.common.dao.GenericAssociationDaoImpl;
import fr.blossom.core.group.Group;
import fr.blossom.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserGroupDaoImpl extends GenericAssociationDaoImpl<User, Group, AssociationUserGroup> implements AssociationUserGroupDao {
    public AssociationUserGroupDaoImpl(AssociationUserGroupRepository repository) {
        super(repository);
    }

    @Override
    protected AssociationUserGroup create() {
        return new AssociationUserGroup();
    }
}
