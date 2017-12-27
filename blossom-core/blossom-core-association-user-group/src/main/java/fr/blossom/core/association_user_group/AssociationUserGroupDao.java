package fr.blossom.core.association_user_group;

import fr.blossom.core.common.dao.AssociationDao;
import fr.blossom.core.group.Group;
import fr.blossom.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserGroupDao extends AssociationDao<User, Group, AssociationUserGroup> {

}
