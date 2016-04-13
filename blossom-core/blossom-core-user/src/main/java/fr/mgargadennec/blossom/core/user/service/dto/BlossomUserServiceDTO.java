package fr.mgargadennec.blossom.core.user.service.dto;

import java.util.List;

import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;
import fr.mgargadennec.blossom.core.user.model.BlossomUserStateEnum;

public class BlossomUserServiceDTO extends BlossomAbstractServiceDTO {
  private String firstname = null;
  private String lastname = null;
  private String email = null;
  private String phone = null;
  private String login = null;
  private String password = null;
  private BlossomUserStateEnum state = null;
  private String function = null;
  private Boolean root = false;
  private List<BlossomUserStateEnum> availableStates;

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

  public void setRoot(boolean isRoot) {
    this.root = isRoot;
  }

  public boolean isRoot() {
    return root;
  }

  public List<BlossomUserStateEnum> getAvailableStates() {
    return availableStates;
  }

  public void setAvailableStates(List<BlossomUserStateEnum> availableStates) {
    this.availableStates = availableStates;
  }

}
