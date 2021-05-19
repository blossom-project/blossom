package com.blossomproject.core.association_user_group;

import com.blossomproject.core.common.dao.BlossomGenericAssociationDaoImpl;
import com.blossomproject.core.group.Group;
import com.blossomproject.core.group.GroupRepository;
import com.blossomproject.core.user.User;
import com.blossomproject.core.user.UserRepository;
import org.mapstruct.factory.Mappers;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserGroupDaoImpl extends BlossomGenericAssociationDaoImpl<User, Group, AssociationUserGroup> implements AssociationUserGroupDao {

    public AssociationUserGroupDaoImpl(UserRepository userRepository,
            GroupRepository groupRepository,
            AssociationUserGroupRepository repository) {
        super(userRepository, groupRepository, repository, Mappers.getMapper(AssociationUserGroupEntityMapper.class));
    }

    @Override
    protected AssociationUserGroup create() {
        return new AssociationUserGroup();
    }
}
