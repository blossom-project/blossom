package fr.mgargadennec.blossom.core.common.configuration.root.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.common.configuration.properties.BlossomCommonProperties;

@Configuration
public class BlossomCommonServiceConfiguration {
	
	@Autowired
	private BlossomCommonProperties properties;


}
