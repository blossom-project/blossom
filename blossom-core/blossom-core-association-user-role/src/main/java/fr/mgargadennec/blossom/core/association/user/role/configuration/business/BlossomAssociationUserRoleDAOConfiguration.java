package fr.mgargadennec.blossom.core.association.user.role.configuration.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.user.role.dao.IBlossomAssociationUserRoleDAO;
import fr.mgargadennec.blossom.core.association.user.role.dao.impl.BlossomAssociationUserRoleDAOImpl;
import fr.mgargadennec.blossom.core.association.user.role.repository.BlossomAssociationUserRoleRepository;

@Configuration
public class BlossomAssociationUserRoleDAOConfiguration {

	@Bean
	public IBlossomAssociationUserRoleDAO blossomAssociationUserRoleDao(BlossomAssociationUserRoleRepository boAssociationUserRoleRepository) {
		return new BlossomAssociationUserRoleDAOImpl(boAssociationUserRoleRepository);
	}

}
