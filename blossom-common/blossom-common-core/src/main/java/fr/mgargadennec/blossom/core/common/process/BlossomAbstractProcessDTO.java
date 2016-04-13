package fr.mgargadennec.blossom.core.common.process;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import fr.mgargadennec.blossom.core.common.model.common.BlossomIdentifiable;

public abstract class BlossomAbstractProcessDTO implements BlossomIdentifiable<Long> {

  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;
  private Date dateCreation;
  private Date dateModification;
  private String userCreation;
  private String userModification;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

}
