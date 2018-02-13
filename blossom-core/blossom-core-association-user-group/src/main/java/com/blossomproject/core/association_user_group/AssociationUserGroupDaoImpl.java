package com.blossomproject.core.association_user_group;

import com.blossomproject.core.common.dao.GenericAssociationDaoImpl;
import com.blossomproject.core.group.Group;
import com.blossomproject.core.user.User;

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
