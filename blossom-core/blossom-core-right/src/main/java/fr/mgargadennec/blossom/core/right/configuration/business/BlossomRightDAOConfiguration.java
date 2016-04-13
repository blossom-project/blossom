package fr.mgargadennec.blossom.core.right.configuration.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.right.dao.IBlossomRightDAO;
import fr.mgargadennec.blossom.core.right.dao.impl.BlossomRightDAOImpl;
import fr.mgargadennec.blossom.core.right.repository.BlossomRightRepository;

@Configuration
public class BlossomRightDAOConfiguration {

	@Bean
	public IBlossomRightDAO blossomRightDao(BlossomRightRepository blossomRightRepository) {
		return new BlossomRightDAOImpl(blossomRightRepository);
	}

}
