package fr.mgargadennec.blossom.core.group.web.resource;

import java.util.Date;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "group", collectionRelation = "groups")
public class BlossomGroupResourceState implements Identifiable<String> {
  private String id = null; // Long id overflow js max number value
  private String name = null;
  private String description;
  private Date dateCreation;
  private Date dateModification;
  private String userCreation;
  private String userModification;

  @Override
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the dateCreation
   */
  public Date getDateCreation() {
    return (dateCreation == null) ? null : (Date) dateCreation.clone();
  }

  /**
   * @param dateCreation the dateCreation to set
   */
  public void setDateCreation(Date dateCreation) {
    this.dateCreation = (dateCreation == null) ? null : (Date) dateCreation.clone();
  }

  /**
   * @return the dateModification
   */
  public Date getDateModification() {
    return (dateModification == null) ? null : (Date) dateModification.clone();
  }

  /**
   * @param dateModification the dateModification to set
   */
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

}
