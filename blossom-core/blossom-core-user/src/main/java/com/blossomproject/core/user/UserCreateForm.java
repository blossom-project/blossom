package com.blossomproject.core.user;

import java.util.Locale;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Created by Maël Gargadennnec on 13/06/2017.
 */
@UniqueIdentifier
@UniqueEmail
public class UserCreateForm {

  @NotBlank(message = "{users.user.validation.firstname.NotBlank.message}")
  private String firstname = "";

  @NotBlank(message = "{users.user.validation.lastname.NotBlank.message}")
  private String lastname = "";

  @NotNull(message = "{users.user.validation.civility.NotNull.message}")
  private User.Civility civility = User.Civility.UNKNOWN;

  @NotBlank(message = "{users.user.validation.identifier.NotBlank.message}")
  private String identifier = "";

  @NotBlank(message = "{users.user.validation.email.NotBlank.message}")
  @Email(message = "{users.user.validation.email.Email.message}")
  private String email = "";

  @NotNull
  private Locale locale = Locale.ENGLISH;

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

  public User.Civility getCivility() {
    return civility;
  }

  public void setCivility(User.Civility civility) {
    this.civility = civility;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

}
