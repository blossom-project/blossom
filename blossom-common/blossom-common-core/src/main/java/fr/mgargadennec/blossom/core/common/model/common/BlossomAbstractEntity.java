package fr.mgargadennec.blossom.core.common.model.common;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BlossomAbstractEntity implements IBlossomEntityDTO<Long> {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "DATE_CREATION", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  @Audited
  @BlossomDiffIgnore
  private Date dateCreation;

  @Column(name = "DATE_MODIFICATION")
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  @Audited
  @BlossomDiffIgnore
  private Date dateModification;

  @Column(name = "USER_CREATION", updatable = false)
  @CreatedBy
  @Audited
  @BlossomDiffIgnore
  private String userCreation;

  @Column(name = "USER_MODIFICATION")
  @LastModifiedBy
  @Audited
  @BlossomDiffIgnore
  private String userModification;

  @PrePersist
  public void ensureId() {
    if (id == null) {
      id = Math.abs(UUID.randomUUID().getLeastSignificantBits());
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

}
