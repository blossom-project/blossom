package fr.mgargadennec.blossom.core.role.process.dto;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;

public class BlossomRoleProcessDTO extends BlossomAbstractProcessDTO {

  private String name = null;
  private String description;

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
