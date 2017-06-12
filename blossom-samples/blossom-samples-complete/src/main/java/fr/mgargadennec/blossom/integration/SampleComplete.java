package fr.mgargadennec.blossom.integration;

import fr.mgargadennec.blossom.autoconfigure.EnableBlossom;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@EnableBlossom
@SpringBootApplication
public class SampleComplete {

    public static void main(String[] args) {
        SpringApplication.run(SampleComplete.class, args);
    }

}
