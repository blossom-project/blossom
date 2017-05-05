package fr.mgargadennec.blossom.autoconfigure.ui.web;

import fr.mgargadennec.blossom.ui.current_user.CurrentUserControllerAdvice;
import fr.mgargadennec.blossom.ui.menu.Menu;
import fr.mgargadennec.blossom.ui.menu.MenuControllerAdvice;
import fr.mgargadennec.blossom.ui.web.HomeController;
import fr.mgargadennec.blossom.ui.web.LoginController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(HomeController.class)
public class WebInterfaceAutoConfiguration {

  @Bean
  public LoginController loginController() {
    return new LoginController();
  }

  @Bean
  public HomeController homeController() {
    return new HomeController();
  }


  @Bean
  public CurrentUserControllerAdvice currentUserControllerAdvice() {
    return new CurrentUserControllerAdvice();
  }

  @Bean
  public MenuControllerAdvice menuControllerAdvice(Menu menu) {
    return new MenuControllerAdvice(menu);
  }


}
