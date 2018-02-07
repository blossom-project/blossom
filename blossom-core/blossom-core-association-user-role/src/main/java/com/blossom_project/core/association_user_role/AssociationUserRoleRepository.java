package com.blossom_project.core.association_user_role;

import com.blossom_project.core.common.repository.AssociationRepository;
import com.blossom_project.core.role.Role;
import com.blossom_project.core.user.User;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface AssociationUserRoleRepository extends AssociationRepository<User, Role, AssociationUserRole> {
}
