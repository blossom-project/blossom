package fr.mgargadennec.blossom.core.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.core.user.constants.BlossomUserConst;

@Entity(name = "BlossomUser")
@Table(name = "BLOSSOM_USER")
@Audited
@BlossomResourceType(BlossomUserConst.BLOSSOM_USER_ENTITY_NAME)
public class BlossomUserPO extends BlossomAbstractEntity {

  @Column(name = "FIRSTNAME", length = 30)
  private String firstname = null;

  @Column(name = "LASTNAME", length = 30)
  private String lastname = null;

  @Column(name = "EMAIL", length = 80, unique = true)
  private String email = null;

  @Column(name = "PHONE", length = 20)
  private String phone = null;

  @Column(name = "STATE")
  @Enumerated(EnumType.STRING)
  private BlossomUserStateEnum state = null;

  @Column(name = "LOGIN", length = 31, unique = true)
  private String login = null;

  @Column(name = "PASSWORD", length = 60)
  @NotAudited
  private String password = null;

  @Column(name = "ROOT")
  private Boolean root = false;

  @Column(name = "FUNCTION", length = 256)
  private String function = null;

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public BlossomUserStateEnum getState() {
    return state;
  }

  public void setState(BlossomUserStateEnum state) {
    this.state = state;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRoot(Boolean isRoot) {
    this.root = isRoot;
  }

  public Boolean isRoot() {
    return root;
  }
}
