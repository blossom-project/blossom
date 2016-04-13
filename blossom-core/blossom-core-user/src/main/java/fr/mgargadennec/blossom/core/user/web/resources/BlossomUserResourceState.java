package fr.mgargadennec.blossom.core.user.web.resources;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import fr.mgargadennec.blossom.core.user.model.BlossomUserStateEnum;

@Relation(value = "user", collectionRelation = "users")
public class BlossomUserResourceState implements Identifiable<Serializable> {

  private String id = null; // Long id overflow js max number value
  private String firstname;
  private String lastname;
  private String email;
  private String phone;
  private String login;
  private BlossomUserStateEnum state;
  private String function;
  private List<BlossomUserStateEnum> availableStates;
  private Date dateCreation;
  private Date dateModification;
  private String userCreation;
  private String userModification;
  private Boolean root = false;

  @JsonUnwrapped
  private Resources<EmbeddedWrapper> embeddeds;

  public String getId() {
    return id;
  }

  public void setId(String resourceId) {
    this.id = resourceId;
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

  public List<BlossomUserStateEnum> getAvailableStates() {
    return availableStates;
  }

  public void setAvailableStates(List<BlossomUserStateEnum> availableStates) {
    this.availableStates = availableStates;
  }

  public Date getDateCreation() {
    return dateCreation == null ? null : (Date) dateCreation.clone();
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation == null ? null : (Date) dateCreation.clone();
  }

  public Date getDateModification() {
    return dateModification == null ? null : (Date) dateModification.clone();
  }

  public void setDateModification(Date dateModification) {
    this.dateModification = dateModification == null ? null : (Date) dateModification.clone();
  }

  public String getUserCreation() {
    return userCreation;
  }

  public void setUserCreation(String userCreation) {
    this.userCreation = userCreation;
  }

  public String getUserModification() {
    return userModification;
  }

  public void setUserModification(String userModification) {
    this.userModification = userModification;
  }

  public void setRoot(Boolean isRoot) {
    this.root = isRoot;
  }

  public Boolean isRoot() {
    return root;
  }

  public Resources<EmbeddedWrapper> getEmbeddeds() {
    return embeddeds;
  }

  public void setEmbeddeds(Resources<EmbeddedWrapper> embeddeds) {
    this.embeddeds = embeddeds;
  }

}
