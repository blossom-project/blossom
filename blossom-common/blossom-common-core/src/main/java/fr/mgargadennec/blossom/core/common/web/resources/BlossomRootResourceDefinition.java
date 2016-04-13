package fr.mgargadennec.blossom.core.common.web.resources;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.plugin.core.Plugin;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;

@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_ROOT_RESOURCE_REGISTRY)
public interface BlossomRootResourceDefinition extends Plugin<Class<? extends Resource>>{
	
	public Link getLink();

}
