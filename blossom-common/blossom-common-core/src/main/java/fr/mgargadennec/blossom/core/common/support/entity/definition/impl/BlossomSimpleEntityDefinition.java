package fr.mgargadennec.blossom.core.common.support.entity.definition.impl;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;

public class BlossomSimpleEntityDefinition implements IBlossomEntityDefinition {

  private String entityName;
  private Class<? extends BlossomAbstractEntity> entityClass;
  private boolean isAssociation;
  private String[] rightNames;

  public BlossomSimpleEntityDefinition(String entityName, Class<? extends BlossomAbstractEntity> entityClass,
      boolean isAssociation, String... rightNames) {
    this.entityClass = entityClass;
    this.entityName = entityName;
    this.isAssociation = isAssociation;
    this.rightNames = rightNames;
  }

  public boolean supports(String delimiter) {
    return getEntityName() != null && getEntityName().equals(delimiter);
  }

  public Class<? extends BlossomAbstractEntity> getEntityClass() {
    return entityClass;
  }

  public String getEntityName() {
    return entityName;
  }

  public String[] getRightNames() {
    return rightNames;
  }

  public boolean isAssociation() {
    return isAssociation;
  }

}
