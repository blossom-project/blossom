package fr.mgargadennec.blossom.core.common.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Resource;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResource;
import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResourceDefinition;

@Controller
@RequestMapping("/")
public class BlossomApiController {

	public PluginRegistry<BlossomRootResourceDefinition, Class<? extends Resource>> resourcesRegistry;
	
	@Autowired
	public BlossomApiController(PluginRegistry<BlossomRootResourceDefinition, Class<? extends Resource>> resourcesRegistry) {
		this.resourcesRegistry = resourcesRegistry;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public BlossomRootResource apiRoot() {
		BlossomRootResource root = new BlossomRootResource();
		
		List<BlossomRootResourceDefinition> rootResourcesDefinitions = resourcesRegistry.getPlugins();
		
		for(BlossomRootResourceDefinition rootResourceDefinition : rootResourcesDefinitions){
			root.add(rootResourceDefinition.getLink());
		}

		return root;
	}

}
