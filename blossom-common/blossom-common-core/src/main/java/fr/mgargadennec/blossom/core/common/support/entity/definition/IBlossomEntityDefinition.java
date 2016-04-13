package fr.mgargadennec.blossom.core.common.support.entity.definition;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;

@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_ENTITY_DEFINITION_REGISTRY)
public interface IBlossomEntityDefinition extends Plugin<String> {

  public Class<? extends BlossomAbstractEntity> getEntityClass();

  public String getEntityName();

  public String[] getRightNames();

  public boolean isAssociation();

}
