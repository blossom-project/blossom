package fr.mgargadennec.blossom.core.user;


import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;

import java.util.Date;

public class UserDTO extends AbstractDTO {

  private String identifier;
  private String email;
  private String firstname;
  private String lastname;
  private String description;
  private boolean activated;
  private String passwordHash;
  private String phone;
  private String function;
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

  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
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
}
