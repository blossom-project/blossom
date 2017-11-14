package fr.blossom.core.association_user_role;

import fr.blossom.core.common.repository.AssociationRepository;
import fr.blossom.core.role.Role;
import fr.blossom.core.user.User;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface AssociationUserRoleRepository extends AssociationRepository<User, Role, AssociationUserRole> {
}
