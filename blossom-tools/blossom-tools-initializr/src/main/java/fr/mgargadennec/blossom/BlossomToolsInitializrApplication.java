package fr.mgargadennec.blossom;

import fr.mgargadennec.blossom.initializr.Initializr;
import fr.mgargadennec.blossom.initializr.ProjectGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlossomToolsInitializrApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlossomToolsInitializrApplication.class, args);
	}


  @Bean
  public Initializr initializr(){
    return new Initializr();
  }

  @Bean
  public ProjectGenerator projectGenerator(Initializr initializr){
    return new ProjectGenerator(initializr);
  }
}
