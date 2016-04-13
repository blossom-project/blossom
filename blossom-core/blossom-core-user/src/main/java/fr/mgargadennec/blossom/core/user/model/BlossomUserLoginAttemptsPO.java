package fr.mgargadennec.blossom.core.user.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.LastModifiedDate;

@Entity(name = "BlossomUserLoginAttempts")
@Table(name = "BLOSSOM_USER_LOGIN_ATTEMPTS")
public class BlossomUserLoginAttemptsPO {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "LOGIN")
  private String login;

  @Column(name = "ATTEMPTS")
  private Integer attempts;

  @Column(name = "LAST_MODIFIED")
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  private Date lastModified;

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

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public Integer getAttempts() {
    return attempts;
  }

  public void setAttempts(Integer attempts) {
    this.attempts = attempts;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

}
