package fr.mgargadennec.blossom.core.association_user_role;

import fr.mgargadennec.blossom.core.common.entity.AbstractAssociationEntity;
import fr.mgargadennec.blossom.core.role.Role;
import fr.mgargadennec.blossom.core.user.User;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bo_association_user_role")
public class AssociationUserRole extends AbstractAssociationEntity<User, Role> {
}
