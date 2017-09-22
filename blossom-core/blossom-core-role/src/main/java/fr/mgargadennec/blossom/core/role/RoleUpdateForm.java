package fr.mgargadennec.blossom.core.role;

import org.hibernate.validator.constraints.NotBlank;

public class RoleUpdateForm {

  public RoleUpdateForm() {
  }

  public RoleUpdateForm(RoleDTO role) {
    this.name = role.getName();
    this.description = role.getDescription();
  }

  @NotBlank(message = "{roles.role.validation.name.NotBlank.message}")
  private String name = "";

  @NotBlank(message = "{roles.role.validation.description.NotBlank.message}")
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
