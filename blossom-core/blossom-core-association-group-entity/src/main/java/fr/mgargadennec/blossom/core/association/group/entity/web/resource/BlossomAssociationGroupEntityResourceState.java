package fr.mgargadennec.blossom.core.association.group.entity.web.resource;

import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "authorization", collectionRelation = "authorizations")
public class BlossomAssociationGroupEntityResourceState implements Identifiable<Serializable> {
  private String id = null; // Long id overflow js max number value
  private Date dateCreation;
  private Date dateModification;
  private String userCreation;
  private String userModification;

  private String groupId;
  private String entityId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getDateCreation() {
    return dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  public Date getDateModification() {
    return dateModification;
  }

  public void setDateModification(Date dateModification) {
    this.dateModification = dateModification;
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

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

}
