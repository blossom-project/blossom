package fr.mgargadennec.blossom.core.association.group.user.web.resource;

import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

@Relation(value = "membership", collectionRelation = "memberships")
public class BlossomAssociationGroupUserResourceState implements Identifiable<Serializable> {

  private String id = null; // Long id overflow js max number value
  private String groupId = null;
  private String userId = null;
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

  /**
   * @return the groupId
   */
  public String getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the userId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(String userId) {
    this.userId = userId;
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

  /**
   * @return the userCreation
   */
  public String getUserCreation() {
    return userCreation;
  }

  /**
   * @param userCreation the userCreation to set
   */
  public void setUserCreation(String userCreation) {
    this.userCreation = userCreation;
  }

  /**
   * @return the userModification
   */
  public String getUserModification() {
    return userModification;
  }

  /**
   * @param userModification the userModification to set
   */
  public void setUserModification(String userModification) {
    this.userModification = userModification;
  }

}