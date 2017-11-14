package fr.blossom.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@SpringBootApplication
public class SampleBasic {
  private final static Logger LOGGER = LoggerFactory.getLogger(SampleBasic.class);

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(SampleBasic.class, args);
    String[] definitions = ctx.getBeanDefinitionNames();
    for (String definition : definitions) {
      LOGGER.info("Bean {} of type {}", definition, ctx.getBean(definition).getClass());
    }
  }
}
