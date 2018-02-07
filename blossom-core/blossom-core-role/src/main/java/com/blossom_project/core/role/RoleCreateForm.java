package com.blossom_project.core.role;

import javax.validation.constraints.NotBlank;


@UniqueRoleName
public class RoleCreateForm {

  @NotBlank(message = "{roles.role.validation.name.NotBlank.message}")
  private String name = "";

  private String description = "";

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

}
