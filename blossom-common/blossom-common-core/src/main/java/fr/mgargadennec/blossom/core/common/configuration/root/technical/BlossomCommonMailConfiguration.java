package fr.mgargadennec.blossom.core.common.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring4.SpringTemplateEngine;

import fr.mgargadennec.blossom.core.common.support.mail.IBlossomMailService;
import fr.mgargadennec.blossom.core.common.support.mail.impl.BlossomMailServiceImpl;

@Configuration
public class BlossomCommonMailConfiguration {
	private String mailFrom = "toto";

	private String[] mailFilters = new String[] { "test" };

	private String basePath = "foobar";

	
	@Bean
	public IBlossomMailService blossomMailService(JavaMailSenderImpl mailSender, SpringTemplateEngine templateEngine) {
		return new BlossomMailServiceImpl(mailSender, templateEngine, mailFrom, mailFilters, basePath);
	}
}
