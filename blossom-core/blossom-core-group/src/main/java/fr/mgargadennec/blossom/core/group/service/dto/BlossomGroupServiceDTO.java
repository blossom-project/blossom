package fr.mgargadennec.blossom.core.group.service.dto;

import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;

public class BlossomGroupServiceDTO extends BlossomAbstractServiceDTO {
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
