package fr.mgargadennec.blossom.core.association_user_group;

import fr.mgargadennec.blossom.core.common.dao.AssociationDao;
import fr.mgargadennec.blossom.core.group.Group;
import fr.mgargadennec.blossom.core.user.User;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface AssociationUserGroupDao extends AssociationDao<User, Group, AssociationUserGroup> {

}
