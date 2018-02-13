package com.blossomproject.core.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;


/**
 * <p>Abstract DTO class for representing a domain entity.</p>
 * <p>This class contains only basic technical informations and should be extending for your domain entities.</p>
 *
 * @author Maël Gargadennec
 */
public abstract class AbstractDTO {
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  private Date creationDate;
  private String creationUser;

  private Date modificationDate;
  private String modificationUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getCreationUser() {
    return creationUser;
  }

  public void setCreationUser(String creationUser) {
    this.creationUser = creationUser;
  }

  public Date getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(Date modificationDate) {
    this.modificationDate = modificationDate;
  }

  public String getModificationUser() {
    return modificationUser;
  }

  public void setModificationUser(String modificationUser) {
    this.modificationUser = modificationUser;
  }

    @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AbstractDTO that = (AbstractDTO) o;

    return id  == null  || that.id == null ? false :  id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
