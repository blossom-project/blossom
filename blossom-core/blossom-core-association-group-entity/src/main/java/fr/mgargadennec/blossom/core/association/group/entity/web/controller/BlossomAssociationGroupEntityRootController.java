package fr.mgargadennec.blossom.core.association.group.entity.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.mgargadennec.blossom.core.association.group.entity.web.resource.BlossomAssociationGroupEntityResourceDefinition;
import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResource;

@RequestMapping("/group_entities")
public class BlossomAssociationGroupEntityRootController {

	public PluginRegistry<BlossomAssociationGroupEntityResourceDefinition, Class<? extends Resource>> resourcesRegistry;

	@Autowired
	public BlossomAssociationGroupEntityRootController(PluginRegistry<BlossomAssociationGroupEntityResourceDefinition, Class<? extends Resource>> resourcesRegistry) {
		this.resourcesRegistry = resourcesRegistry;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public BlossomRootResource apiRoot() {
		BlossomRootResource associationGroupEntityRoot = new BlossomRootResource();

		List<BlossomAssociationGroupEntityResourceDefinition> associationGroupEntitiesResourcesDefinitions = resourcesRegistry.getPlugins();

		for (BlossomAssociationGroupEntityResourceDefinition rootResourceDefinition : associationGroupEntitiesResourcesDefinitions) {
			associationGroupEntityRoot.add(rootResourceDefinition.getLink());
		}

		return associationGroupEntityRoot;
	}
}
