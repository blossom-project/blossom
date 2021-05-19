package com.blossomproject.core.association_user_role;

import com.blossomproject.core.common.dao.BlossomGenericAssociationDaoImpl;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.role.Role;
import com.blossomproject.core.role.RoleRepository;
import com.blossomproject.core.user.User;
import com.blossomproject.core.user.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserRoleDaoImpl extends BlossomGenericAssociationDaoImpl<User, Role, AssociationUserRole> implements AssociationUserRoleDao {

    private final AssociationUserRoleRepository repository;

    public AssociationUserRoleDaoImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            AssociationUserRoleRepository repository) {
        super(userRepository, roleRepository, repository, Mappers.getMapper(AssociationUserRoleEntityMapper.class));
        this.repository = repository;
    }

    @Override
    protected AssociationUserRole create() {
        return new AssociationUserRole();
    }

  @Override
  public boolean getUserExistsByPrivilege(List<Privilege> privileges) {
    return repository.exists(
      privileges
        .stream()
        .map(privilege -> QAssociationUserRole.associationUserRole.b.privileges.contains(privilege.privilege()))
        .reduce(QAssociationUserRole.associationUserRole.a.activated.isTrue(), BooleanExpression::and)
    );
  }
}
