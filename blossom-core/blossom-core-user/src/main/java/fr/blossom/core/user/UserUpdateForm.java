package fr.blossom.core.user;

import fr.blossom.core.user.User.Civility;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Created by Maël Gargadennnec on 13/06/2017.
 */

@UniqueEmail(idField = "id")
public class UserUpdateForm {

  private Long id;

  @NotBlank(message = "{users.user.validation.firstname.NotBlank.message}")
  private String firstname = "";

  @NotBlank(message = "{users.user.validation.lastname.NotBlank.message}")
  private String lastname = "";

  @NotNull(message = "{users.user.validation.civility.NotNull.message}")
  private User.Civility civility = User.Civility.UNKNOWN;

  @NotBlank(message = "{users.user.validation.email.NotBlank.message}")
  @Email(message = "{users.user.validation.email.Email.message}")
  private String email = "";

  private String description;
  private String phone;
  private String company;
  private String function;
  private boolean activated;

  public UserUpdateForm() {
  }

  public UserUpdateForm(UserDTO user) {
    this.id = user.getId();
    this.activated = user.isActivated();
    this.firstname = user.getFirstname();
    this.lastname = user.getLastname();
    this.description = user.getDescription();
    this.civility = user.getCivility();
    this.company = user.getCompany();
    this.function = user.getFunction();
    this.email = user.getEmail();
    this.phone = user.getPhone();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Civility getCivility() {
    return civility;
  }

  public void setCivility(Civility civility) {
    this.civility = civility;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
}
