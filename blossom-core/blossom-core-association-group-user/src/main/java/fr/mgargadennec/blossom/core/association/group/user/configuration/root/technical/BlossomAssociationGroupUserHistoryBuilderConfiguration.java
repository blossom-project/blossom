package fr.mgargadennec.blossom.core.association.group.user.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.group.user.support.history.builder.impl.BlossomAssociationGroupUserHistoryBuilderImpl;
import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.group.repository.BlossomGroupRepository;
import fr.mgargadennec.blossom.core.user.repository.BlossomUserRepository;

@Configuration
public class BlossomAssociationGroupUserHistoryBuilderConfiguration {

	@Bean
	public BlossomAssociationGroupUserHistoryBuilderImpl blossomAssociationGroupUserHistoryBuilderImpl(
			IBlossomEntityDiffBuilder blossomEntityDiffBuilder,
			IBlossomHistoryDAO blossomHistoryDAO,
			BlossomGroupRepository blossomGroupRepository,
			BlossomUserRepository blossomUserRepository) {
		return new BlossomAssociationGroupUserHistoryBuilderImpl(blossomEntityDiffBuilder, blossomHistoryDAO,blossomGroupRepository, blossomUserRepository);
	}

}
