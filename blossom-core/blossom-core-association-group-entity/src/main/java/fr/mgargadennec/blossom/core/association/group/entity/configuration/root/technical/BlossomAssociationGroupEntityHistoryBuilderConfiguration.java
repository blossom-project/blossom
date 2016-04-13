package fr.mgargadennec.blossom.core.association.group.entity.configuration.root.technical;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;

import fr.mgargadennec.blossom.core.association.group.entity.support.history.builder.impl.BlossomAssociationGroupEntityHistoryBuilderImpl;
import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;

@Configuration
public class BlossomAssociationGroupEntityHistoryBuilderConfiguration {

	@Bean
	public BlossomAssociationGroupEntityHistoryBuilderImpl blossomAssociationGroupEntityHistoryBuilderImpl(
			IBlossomEntityDiffBuilder blossomEntityDiffBuilder, 
			IBlossomHistoryDAO blossomHistoryDAO,
			@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_ENTITY_DEFINITION_REGISTRY) PluginRegistry<IBlossomEntityDefinition, String> entityBuilderRegistry) {
		return new BlossomAssociationGroupEntityHistoryBuilderImpl(blossomEntityDiffBuilder,blossomHistoryDAO,entityBuilderRegistry);
	}

}
