package fr.mgargadennec.blossom.core.role.configuration.root.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.role.dao.IBlossomRoleDAO;
import fr.mgargadennec.blossom.core.role.process.IBlossomRoleProcess;
import fr.mgargadennec.blossom.core.role.process.impl.BlossomRoleProcessImpl;

@Configuration
public class BlossomRoleProcessConfiguration {
	@Bean
	public IBlossomRoleProcess blossomRoleProcess(IBlossomRoleDAO blossomRoleDAO) {
		return new BlossomRoleProcessImpl(blossomRoleDAO);
	}

}
