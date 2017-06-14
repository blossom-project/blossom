package fr.mgargadennec.blossom.core.user;

/**
 * Created by Maël Gargadennnec on 13/06/2017.
 */
public class UserCreateForm {
  private String firstname = "";
  private String lastname = "";
  private User.Civility civility = User.Civility.UNKNOWN;
  private String identifier = "";
  private String email = "";

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
}
