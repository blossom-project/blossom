package com.blossom_project.core.association_user_group;

import com.blossom_project.core.common.repository.AssociationRepository;
import com.blossom_project.core.group.Group;
import com.blossom_project.core.user.User;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface AssociationUserGroupRepository extends AssociationRepository<User, Group, AssociationUserGroup> {
}
