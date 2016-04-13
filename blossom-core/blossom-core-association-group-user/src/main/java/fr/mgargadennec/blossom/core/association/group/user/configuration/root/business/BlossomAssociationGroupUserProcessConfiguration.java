package fr.mgargadennec.blossom.core.association.group.user.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.group.user.dao.IBlossomAssociationGroupUserDAO;
import fr.mgargadennec.blossom.core.association.group.user.process.IBlossomAssociationGroupUserProcess;
import fr.mgargadennec.blossom.core.association.group.user.process.impl.BlossomAssociationGroupUserProcessImpl;

@Configuration
public class BlossomAssociationGroupUserProcessConfiguration {

	@Bean
	public IBlossomAssociationGroupUserProcess blossomAssociationGroupUserProcess(IBlossomAssociationGroupUserDAO blossomAssociationGroupUserDAO) {
		return new BlossomAssociationGroupUserProcessImpl(blossomAssociationGroupUserDAO);
	}

}
