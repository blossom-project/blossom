package fr.mgargadennec.blossom.core.association.user.role.configuration.root.technical;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;

import fr.mgargadennec.blossom.core.association.user.role.support.history.builder.impl.BlossomAssociationRoleRightHistoryBuilderImpl;
import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.role.repository.BlossomRoleRepository;

@Configuration
public class BlossomAssociationRoleRightHistoryBuilderConfiguration {

	@Bean
	public BlossomAssociationRoleRightHistoryBuilderImpl blossomAssociationRoleRightHistoryBuilderImpl(
			IBlossomEntityDiffBuilder blossomEntityDiffBuilder, 
			BlossomRoleRepository blossomRoleRepository, 
			IBlossomHistoryDAO blossomHistoryDAO,
			@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_ENTITY_DEFINITION_REGISTRY) PluginRegistry<IBlossomEntityDefinition, String> entityBuilderRegistry) {
		return new BlossomAssociationRoleRightHistoryBuilderImpl(blossomEntityDiffBuilder,blossomRoleRepository,blossomHistoryDAO);
	}

}
