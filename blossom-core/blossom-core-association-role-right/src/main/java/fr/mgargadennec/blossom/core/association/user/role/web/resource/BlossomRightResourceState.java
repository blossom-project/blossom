package fr.mgargadennec.blossom.core.association.user.role.web.resource;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;

@Relation(value = "right", collectionRelation = "rights")
public class BlossomRightResourceState implements Identifiable<Serializable> {
  private String id = null; // Long id overflow js max number value
  private String roleId = null;
  private Date dateCreation;
  private Date dateModification;
  private String userCreation;
  private String userModification;

  private List<BlossomRightPermissionEnum> permissions;
  private String resource;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<BlossomRightPermissionEnum> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<BlossomRightPermissionEnum> permissions) {
    this.permissions = permissions;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
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

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

}
