package fr.mgargadennec.blossom.core.group.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.group.dao.IBlossomGroupDAO;
import fr.mgargadennec.blossom.core.group.dao.impl.BlossomGroupDAOImpl;
import fr.mgargadennec.blossom.core.group.repository.BlossomGroupRepository;

@Configuration
public class BlossomGroupDAOConfiguration {

	@Bean
	public IBlossomGroupDAO blossomGroupDao(BlossomGroupRepository boGroupRepository) {
		return new BlossomGroupDAOImpl(boGroupRepository);
	}

}
