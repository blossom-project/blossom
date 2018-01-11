package fr.blossom.core.association_user_role;

import fr.blossom.core.common.entity.AbstractAssociationEntity;
import fr.blossom.core.role.Role;
import fr.blossom.core.user.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "blossom_association_user_role")
public class AssociationUserRole extends AbstractAssociationEntity<User, Role> {

  @ManyToOne(fetch = FetchType.EAGER)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "user_id",referencedColumnName = "id")
  private User a;

  @ManyToOne(fetch = FetchType.EAGER)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "role_id",referencedColumnName = "id")
  private Role b;

  @Override
  public User getA() {
    return this.a;
  }

  @Override
  public void setA(User user) {
    this.a = user;
  }

  @Override
  public Role getB() {
    return this.b;
  }

  @Override
  public void setB(Role group) {
    this.b = group;
  }
}
