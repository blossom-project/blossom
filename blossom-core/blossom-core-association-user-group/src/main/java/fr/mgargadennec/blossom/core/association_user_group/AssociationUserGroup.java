package fr.mgargadennec.blossom.core.association_user_group;

import fr.mgargadennec.blossom.core.common.entity.AbstractAssociationEntity;
import fr.mgargadennec.blossom.core.group.Group;
import fr.mgargadennec.blossom.core.user.User;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bo_association_user_group")
public class AssociationUserGroup extends AbstractAssociationEntity<User, Group> {
}
