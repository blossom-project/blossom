package com.blossomproject.core.association_user_role;

import com.blossomproject.core.common.repository.AssociationRepository;
import com.blossomproject.core.role.Role;
import com.blossomproject.core.user.User;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface AssociationUserRoleRepository extends AssociationRepository<User, Role, AssociationUserRole> {
}
