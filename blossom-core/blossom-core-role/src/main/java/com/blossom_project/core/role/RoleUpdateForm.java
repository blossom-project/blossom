package com.blossom_project.core.role;

import javax.validation.constraints.NotBlank;


@UniqueRoleName(idField = "id")
public class RoleUpdateForm {

  private Long id;

  @NotBlank(message = "{roles.role.validation.name.NotBlank.message}")
  private String name = "";

  private String description = "";


  public RoleUpdateForm() {
  }

  public RoleUpdateForm(RoleDTO role) {
    this.id = role.getId();
    this.name = role.getName();
    this.description = role.getDescription();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
