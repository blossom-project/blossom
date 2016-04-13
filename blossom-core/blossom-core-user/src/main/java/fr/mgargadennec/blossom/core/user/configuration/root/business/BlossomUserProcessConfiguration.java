package fr.mgargadennec.blossom.core.user.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.user.dao.IBlossomUserDAO;
import fr.mgargadennec.blossom.core.user.process.IBlossomUserProcess;
import fr.mgargadennec.blossom.core.user.process.impl.BlossomUserProcessImpl;

@Configuration
public class BlossomUserProcessConfiguration {
	@Bean
	public IBlossomUserProcess blossomUserProcess(IBlossomUserDAO blossomUserDAO) {
		return new BlossomUserProcessImpl(blossomUserDAO);
	}

}
