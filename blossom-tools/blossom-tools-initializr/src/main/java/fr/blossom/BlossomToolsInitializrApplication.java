package fr.blossom;

import fr.blossom.initializr.Initializr;
import fr.blossom.initializr.ProjectGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

@SpringBootApplication
public class BlossomToolsInitializrApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlossomToolsInitializrApplication.class, args);
  }

  @Bean
  public Initializr initializr() {
    return new Initializr();
  }

  @Bean
  public ProjectGenerator projectGenerator(Initializr initializr, ResourceLoader resourceLoader) {
    return new ProjectGenerator(initializr, resourceLoader);
  }
}
