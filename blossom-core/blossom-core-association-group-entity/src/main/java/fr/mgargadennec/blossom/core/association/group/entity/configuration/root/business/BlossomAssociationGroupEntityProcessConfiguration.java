package fr.mgargadennec.blossom.core.association.group.entity.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.group.entity.dao.IBlossomAssociationGroupEntityDAO;
import fr.mgargadennec.blossom.core.association.group.entity.process.IBlossomAssociationGroupEntityProcess;
import fr.mgargadennec.blossom.core.association.group.entity.process.impl.BlossomAssociationGroupEntityProcessImpl;

@Configuration
public class BlossomAssociationGroupEntityProcessConfiguration {
	@Bean
	public IBlossomAssociationGroupEntityProcess blossomAssociationGroupEntityProcess(IBlossomAssociationGroupEntityDAO blossomRoleDAO) {
		return new BlossomAssociationGroupEntityProcessImpl(blossomRoleDAO);
	}

}
