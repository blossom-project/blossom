package fr.blossom.core.group;

import java.util.Locale;

import org.hibernate.validator.constraints.NotBlank;

public class GroupCreateForm {

  @NotBlank(message = "{groups.group.validation.name.NotBlank.message}")
  private String name = "";

  @NotBlank(message = "{groups.group.validation.description.NotBlank.message}")
  private String description = "";

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
