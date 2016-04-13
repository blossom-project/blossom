package fr.mgargadennec.blossom.core.right.configuration.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.right.dao.IBlossomRightDAO;
import fr.mgargadennec.blossom.core.right.process.IBlossomRightProcess;
import fr.mgargadennec.blossom.core.right.process.impl.BlossomRightProcessImpl;

@Configuration
public class BlossomRightProcessConfiguration {
	@Bean
	public IBlossomRightProcess blossomRightProcess(IBlossomRightDAO blossomRightDAO) {
		return new BlossomRightProcessImpl(blossomRightDAO);
	}

}
