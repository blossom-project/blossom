package com.blossom_project.core.user;

import com.blossom_project.core.common.entity.AbstractEntity;
import com.blossom_project.core.common.entity.converter.LocaleConverter;

import javax.persistence.*;
import java.util.Date;
import java.util.Locale;

@Entity @Table(name = "blossom_user")
public class User extends AbstractEntity {

  @Column(name = "identifier", nullable = true, unique = true)
  private String identifier;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Lob @Column(name = "description")
  private String description;

  @Column(name = "activated", nullable = false)
  private boolean activated;

  @Column(name = "last_connection", nullable = true) @Temporal(TemporalType.TIMESTAMP)
  private Date lastConnection;

  @Column(name = "civility") @Enumerated(EnumType.STRING)
  private Civility civility;

  @Column(name = "firstname", nullable = false)
  private String firstname;

  @Column(name = "lastname", nullable = false)
  private String lastname;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "company")
  private String company;

  @Column(name = "function")
  private String function;

  @Lob
  @Column(name = "avatar")
  private byte[] avatar;

  @Convert(converter = LocaleConverter.class)
  @Column(name = "locale", nullable = false)
  private Locale locale;

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public Date getLastConnection() {
    return lastConnection;
  }

  public void setLastConnection(Date lastConnection) {
    this.lastConnection = lastConnection;
  }

  public Civility getCivility() {
    return civility;
  }

  public void setCivility(Civility civility) {
    this.civility = civility;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public byte[] getAvatar() {
    return avatar;
  }

  public void setAvatar(byte[] avatar) {
    this.avatar = avatar;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public enum Civility {
    UNKNOWN, MAN, WOMAN;
  }

  @Override public String toString() {
    return "User{" + "id=" + getId() + ", email='" + email.replaceFirst("@.*", "@***") + ", passwordHash='"
      + passwordHash.substring(0, 10) + '}';
  }

}
