package fr.mgargadennec.blossom.core.group.process.dto;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;

public class BlossomGroupProcessDTO extends BlossomAbstractProcessDTO {

  private String name;
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
