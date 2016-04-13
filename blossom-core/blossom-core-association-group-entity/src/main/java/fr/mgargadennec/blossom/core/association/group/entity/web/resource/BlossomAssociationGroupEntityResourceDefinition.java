package fr.mgargadennec.blossom.core.association.group.entity.web.resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.plugin.core.Plugin;

import fr.mgargadennec.blossom.core.association.group.entity.constants.BlossomAssociationGroupEntityConst;

@Qualifier(BlossomAssociationGroupEntityConst.PLUGIN_QUALIFIER_BLOSSOM_ASSOCIATION_GROUP_ENTITY_RESOURCE_REGISTRY)
public interface BlossomAssociationGroupEntityResourceDefinition extends Plugin<Class<? extends Resource>> {

	public Link getLink();

}
