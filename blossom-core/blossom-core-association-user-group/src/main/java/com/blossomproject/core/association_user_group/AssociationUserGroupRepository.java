package com.blossomproject.core.association_user_group;

import com.blossomproject.core.common.repository.AssociationRepository;
import com.blossomproject.core.group.Group;
import com.blossomproject.core.user.User;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface AssociationUserGroupRepository extends AssociationRepository<User, Group, AssociationUserGroup> {
}
