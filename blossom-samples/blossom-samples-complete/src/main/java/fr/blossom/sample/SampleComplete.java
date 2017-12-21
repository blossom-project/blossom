package fr.blossom.sample;

import fr.blossom.autoconfigure.EnableBlossom;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@EnableBlossom
@SpringBootApplication
public class SampleComplete {

  public static void main(String[] args) {
        SpringApplication.run(SampleComplete.class, args);
    }

  @Bean
  @Order(0)
  public MenuItem testMenuItem(MenuItemBuilder builder) {
    return builder.key("test").label("menu.test").icon("fa fa-photo").link("/blossom/test").build();
  }

  @Bean
  public MenuItem testManagerMenuItem(MenuItemBuilder builder, @Qualifier("testMenuItem") MenuItem testMenuItem) {
    return builder.key("pouet").label("menu.test.pouet").link("/blossom/test/pouet").icon("fa fa-photo").order(0).parent(testMenuItem).build();
  }
}
