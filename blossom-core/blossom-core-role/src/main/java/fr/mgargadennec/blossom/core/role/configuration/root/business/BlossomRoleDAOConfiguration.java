package fr.mgargadennec.blossom.core.role.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.role.dao.IBlossomRoleDAO;
import fr.mgargadennec.blossom.core.role.dao.impl.BlossomRoleDAOImpl;
import fr.mgargadennec.blossom.core.role.repository.BlossomRoleRepository;

@Configuration
public class BlossomRoleDAOConfiguration {

	@Bean
	public IBlossomRoleDAO blossomRoleDao(BlossomRoleRepository boRoleRepository) {
		return new BlossomRoleDAOImpl(boRoleRepository);
	}

}
