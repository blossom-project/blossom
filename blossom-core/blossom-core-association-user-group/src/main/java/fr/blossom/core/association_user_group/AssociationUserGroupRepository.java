package fr.blossom.core.association_user_group;

import fr.blossom.core.common.repository.AssociationRepository;
import fr.blossom.core.group.Group;
import fr.blossom.core.user.User;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface AssociationUserGroupRepository extends AssociationRepository<User, Group, AssociationUserGroup> {
}
