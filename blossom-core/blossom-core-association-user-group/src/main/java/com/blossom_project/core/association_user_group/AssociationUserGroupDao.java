package com.blossom_project.core.association_user_group;

import com.blossom_project.core.common.dao.AssociationDao;
import com.blossom_project.core.group.Group;
import com.blossom_project.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserGroupDao extends AssociationDao<User, Group, AssociationUserGroup> {

}
