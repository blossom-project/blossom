package fr.blossom.samples;

import fr.blossom.autoconfigure.EnableBlossom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@SpringBootApplication
@EnableBlossom
public class SampleFileManager {
  private final static Logger LOGGER = LoggerFactory.getLogger(SampleFileManager.class);

  public static void main(String[] args) {
    SpringApplication.run(SampleFileManager.class, args);
  }
}
