package fr.mgargadennec.blossom.core.association.user.role.configuration.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.user.role.dao.IBlossomAssociationUserRoleDAO;
import fr.mgargadennec.blossom.core.association.user.role.process.IBlossomAssociationUserRoleProcess;
import fr.mgargadennec.blossom.core.association.user.role.process.impl.BlossomAssociationUserRoleProcessImpl;

@Configuration
public class BlossomAssociationUserRoleProcessConfiguration {
	@Bean
	public IBlossomAssociationUserRoleProcess blossomAssociationUserRoleProcess(IBlossomAssociationUserRoleDAO blossomAssociationUserRoleDAO) {
		return new BlossomAssociationUserRoleProcessImpl(blossomAssociationUserRoleDAO);
	}

}
