package com.blossomproject.core.group;

import com.blossomproject.core.common.dto.AbstractDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GroupDTO extends AbstractDTO {
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
