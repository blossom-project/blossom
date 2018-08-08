package com.blossomproject.core.association_user_role;

import com.blossomproject.core.common.dao.GenericAssociationDaoImpl;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.role.Role;
import com.blossomproject.core.user.User;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class AssociationUserRoleDaoImpl extends GenericAssociationDaoImpl<User, Role, AssociationUserRole> implements AssociationUserRoleDao {

  private final AssociationUserRoleRepository repository;

  public AssociationUserRoleDaoImpl(AssociationUserRoleRepository repository) {
    super(repository);
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
