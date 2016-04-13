package fr.mgargadennec.blossom.core.association.user.role.configuration.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.user.role.support.history.builder.impl.BlossomAssociationUserRoleHistoryBuilderImpl;
import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;

@Configuration
public class BlossomAssociationUserRoleHistoryBuilderConfiguration {

	@Bean
	public BlossomAssociationUserRoleHistoryBuilderImpl blossomAssociationUserRoleHistoryBuilderImpl(
			IBlossomEntityDiffBuilder blossomEntityDiffBuilder,
			IBlossomHistoryDAO blossomHistoryDAO) {
		return new BlossomAssociationUserRoleHistoryBuilderImpl(blossomEntityDiffBuilder,blossomHistoryDAO);
	}

}
