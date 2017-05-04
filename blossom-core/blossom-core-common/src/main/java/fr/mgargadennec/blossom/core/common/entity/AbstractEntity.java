package fr.mgargadennec.blossom.core.common.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractEntity implements Serializable {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "DATE_CREATION", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  private Date dateCreation;

  @Column(name = "DATE_MODIFICATION")
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  private Date dateModification;

  @Column(name = "USER_CREATION", updatable = false)
  @CreatedBy
  private String userCreation;

  @Column(name = "USER_MODIFICATION")
  @LastModifiedBy
  private String userModification;

  @PrePersist
  public void ensureId() {
    if (this.id == null) {
      this.id = Math.abs(UUID.randomUUID().getLeastSignificantBits());
    }
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  public Date getDateModification() {
    return this.dateModification;
  }

  public void setDateModification(Date dateModification) {
    this.dateModification = dateModification;
  }

  public String getUserCreation() {
    return this.userCreation;
  }

  public void setUserCreation(String userCreation) {
    this.userCreation = userCreation;
  }

  public String getUserModification() {
    return this.userModification;
  }

  public void setUserModification(String userModification) {
    this.userModification = userModification;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    AbstractEntity other = (AbstractEntity) obj;
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    return true;
  }

}
