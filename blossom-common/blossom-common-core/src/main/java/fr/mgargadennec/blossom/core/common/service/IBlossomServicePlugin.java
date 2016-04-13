package fr.mgargadennec.blossom.core.common.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;

@Qualifier(value = BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_SERVICE_BUILDER_REGISTRY)
public interface IBlossomServicePlugin extends Plugin<String> {

  IBlossomServiceDTO get(Long id);
  
}
