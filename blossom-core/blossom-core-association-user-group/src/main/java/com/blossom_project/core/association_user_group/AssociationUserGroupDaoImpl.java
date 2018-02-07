package com.blossom_project.core.association_user_group;

import com.blossom_project.core.common.dao.GenericAssociationDaoImpl;
import com.blossom_project.core.group.Group;
import com.blossom_project.core.user.User;

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
