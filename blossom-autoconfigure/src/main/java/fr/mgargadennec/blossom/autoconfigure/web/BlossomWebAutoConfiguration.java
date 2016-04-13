package fr.mgargadennec.blossom.autoconfigure.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fr.mgargadennec.blossom.autoconfigure.EnableBlossomWeb;

@Configuration
@EnableBlossomWeb
@EnableWebMvc
@EnableHypermediaSupport(type = HypermediaType.HAL)
public class BlossomWebAutoConfiguration {

}