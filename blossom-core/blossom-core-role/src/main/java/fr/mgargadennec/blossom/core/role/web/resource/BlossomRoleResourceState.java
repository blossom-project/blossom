package fr.mgargadennec.blossom.core.role.web.resource;

import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "role", collectionRelation = "roles")
public class BlossomRoleResourceState implements Identifiable<Serializable> {
  private String id = null; // Long id overflow js max number value
  private String name;
  private String description;
  private Date dateCreation;
  private Date dateModification;
  private String userCreation;
  private String userModification;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDateCreation() {
    return (dateCreation == null) ? null : (Date) dateCreation.clone();
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = (dateCreation == null) ? null : (Date) dateCreation.clone();
  }

  public Date getDateModification() {
    return (dateModification == null) ? null : (Date) dateModification.clone();
  }

  public void setDateModification(Date dateModification) {
    this.dateModification = (dateModification == null) ? null : (Date) dateModification.clone();
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
