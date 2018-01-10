package fr.blossom.core.group;

import org.hibernate.validator.constraints.NotBlank;

@UniqueGroupName(idField = "id")
public class GroupUpdateForm {

  private Long id;

  @NotBlank(message = "{groups.group.validation.name.NotBlank.message}")
  private String name = "";

  private String description = "";

  public GroupUpdateForm() {
  }

  public GroupUpdateForm(GroupDTO group) {
    this.id = group.getId();
    this.name = group.getName();
    this.description = group.getDescription();
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
