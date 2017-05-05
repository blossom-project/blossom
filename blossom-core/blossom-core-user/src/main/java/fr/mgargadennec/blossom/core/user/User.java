package fr.mgargadennec.blossom.core.user;

import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "user")
@Table(name = "user")
public class User extends AbstractEntity {

  @Column(name = "identifier", nullable = true, unique = true)
  private String identifier;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "firstname", nullable = false)
  private String firstname;

  @Column(name = "lastname", nullable = false)
  private String lastname;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "activated", nullable = false)
  private boolean activated;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "phone", nullable = true)
  private String phone;

  @Column(name = "function", nullable = true)
  private String function;

  @Column(name = "last_connection", nullable = true)
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastConnection;

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public Date getLastConnection() {
    return lastConnection;
  }

  public void setLastConnection(Date lastConnection) {
    this.lastConnection = lastConnection;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + getId() + ", email='" + email.replaceFirst("@.*", "@***") + ", passwordHash='" + passwordHash.substring(0, 10) + '}';
  }

}
