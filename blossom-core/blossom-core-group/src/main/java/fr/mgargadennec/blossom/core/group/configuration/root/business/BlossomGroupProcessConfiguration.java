package fr.mgargadennec.blossom.core.group.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.group.dao.IBlossomGroupDAO;
import fr.mgargadennec.blossom.core.group.process.IBlossomGroupProcess;
import fr.mgargadennec.blossom.core.group.process.impl.BlossomGroupProcessImpl;

@Configuration
public class BlossomGroupProcessConfiguration {
	@Bean
	public IBlossomGroupProcess blossomGroupProcess(IBlossomGroupDAO blossomGroupDAO) {
		return new BlossomGroupProcessImpl(blossomGroupDAO);
	}

}
