package fr.mgargadennec.blossom.core.role;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class RoleUpdateForm {

  public RoleUpdateForm() {
  }

  public RoleUpdateForm(String name, String description, Locale locale) {
    this.name = name;
    this.description = description;
    this.locale = locale;
  }

  @NotBlank(message = "{roles.role.validation.name.NotBlank.message}")
  private String name = "";

  @NotBlank(message = "{roles.role.validation.description.NotBlank.message}")
  private String description = "";

  @NotNull
  private Locale locale = Locale.ENGLISH;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

}
