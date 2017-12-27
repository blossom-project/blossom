package fr.blossom.sample;

import fr.blossom.autoconfigure.EnableBlossom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBlossom
public class IntegrationApplicationUI {
  private final static Logger LOGGER = LoggerFactory.getLogger(IntegrationApplicationUI.class);

  public static void main(String[] args) {
    SpringApplication.run(IntegrationApplicationUI.class, args);
  }
}
