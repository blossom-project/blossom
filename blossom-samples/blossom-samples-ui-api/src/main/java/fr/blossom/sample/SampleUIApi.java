package fr.blossom.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@SpringBootApplication
public class SampleUIApi {
  private final static Logger LOGGER = LoggerFactory.getLogger(SampleUIApi.class);

  public static void main(String[] args) {
    SpringApplication.run(SampleUIApi.class, args);
  }
}
