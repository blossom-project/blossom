package fr.mgargadennec.blossom.core.common.support.indexation.builder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;

@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_REGISTRY)
public interface IBlossomESResourceIndexBuilder extends Plugin<String> {

  public BlossomIndexInfosDTO getIndexInfos();

}
