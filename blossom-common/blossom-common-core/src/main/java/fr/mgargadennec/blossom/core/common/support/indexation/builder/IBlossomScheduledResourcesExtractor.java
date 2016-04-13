package fr.mgargadennec.blossom.core.common.support.indexation.builder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomIdentifiable;

@Qualifier(value = BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_EXTRACTOR_BUILDER_REGISTRY)
public interface IBlossomScheduledResourcesExtractor extends Plugin<Class<?>> {

  void indexFull() throws JsonProcessingException;

  void indexOne(BlossomIdentifiable<Long> entity);

}