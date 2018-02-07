package com.blossom_project.core.group;

import java.util.Locale;

import javax.validation.constraints.NotBlank;

@UniqueGroupName
public class GroupCreateForm {

  @NotBlank(message = "{groups.group.validation.name.NotBlank.message}")
  private String name = "";

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
