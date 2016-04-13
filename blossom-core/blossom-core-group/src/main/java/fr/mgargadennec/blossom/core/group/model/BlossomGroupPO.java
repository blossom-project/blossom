package fr.mgargadennec.blossom.core.group.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.core.group.constants.BlossomGroupConst;

@Entity(name = "BlossomGroup")
@Table(name = "BLOSSOM_GROUP")
@Audited
@BlossomResourceType(BlossomGroupConst.BLOSSOM_GROUP_ENTITY_NAME)
public class BlossomGroupPO extends BlossomAbstractEntity {

  @Column(name = "NAME", length = 30)
  private String name;

  @Column(name = "DESCRIPTION")
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
