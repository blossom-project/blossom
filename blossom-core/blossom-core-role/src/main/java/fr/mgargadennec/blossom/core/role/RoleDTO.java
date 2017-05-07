package fr.mgargadennec.blossom.core.role;

import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;

public class RoleDTO extends AbstractDTO {
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
