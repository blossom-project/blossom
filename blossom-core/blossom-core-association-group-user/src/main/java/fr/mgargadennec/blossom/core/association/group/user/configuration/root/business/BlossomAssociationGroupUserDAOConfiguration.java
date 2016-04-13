package fr.mgargadennec.blossom.core.association.group.user.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.group.user.dao.IBlossomAssociationGroupUserDAO;
import fr.mgargadennec.blossom.core.association.group.user.dao.impl.BlossomAssociationGroupUserDAOImpl;
import fr.mgargadennec.blossom.core.association.group.user.repository.BlossomAssociationGroupUserRepository;

@Configuration
public class BlossomAssociationGroupUserDAOConfiguration {

	@Bean
	public IBlossomAssociationGroupUserDAO blossomAssociationGroupUserDao(BlossomAssociationGroupUserRepository boAssociationGroupUserRepository) {
		return new BlossomAssociationGroupUserDAOImpl(boAssociationGroupUserRepository);
	}

}
