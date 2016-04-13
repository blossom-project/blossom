package fr.mgargadennec.blossom.core.user.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.user.dao.IBlossomUserDAO;
import fr.mgargadennec.blossom.core.user.dao.impl.BlossomUserDAOImpl;
import fr.mgargadennec.blossom.core.user.repository.BlossomUserRepository;

@Configuration
public class BlossomUserDAOConfiguration {

	@Bean
	public IBlossomUserDAO blossomUserDao(BlossomUserRepository boUserRepository) {
		return new BlossomUserDAOImpl(boUserRepository);
	}

}
