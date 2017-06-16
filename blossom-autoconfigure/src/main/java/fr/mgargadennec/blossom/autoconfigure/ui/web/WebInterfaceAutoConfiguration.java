package fr.mgargadennec.blossom.autoconfigure.ui.web;

import fr.mgargadennec.blossom.core.common.utils.action_token.ActionTokenService;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.current_user.CurrentUserControllerAdvice;
import fr.mgargadennec.blossom.ui.i18n.LocaleControllerAdvice;
import fr.mgargadennec.blossom.ui.menu.Menu;
import fr.mgargadennec.blossom.ui.menu.MenuControllerAdvice;
import fr.mgargadennec.blossom.ui.web.ActivationController;
import fr.mgargadennec.blossom.ui.web.HomeController;
import fr.mgargadennec.blossom.ui.web.LoginController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(HomeController.class)
@PropertySource({"classpath:/languages.properties"})
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
  public ActivationController activationController(ActionTokenService tokenService, UserService userService) {
    return new ActivationController(tokenService, userService);
  }

  @Bean
  public CurrentUserControllerAdvice currentUserControllerAdvice() {
    return new CurrentUserControllerAdvice();
  }

  @Bean
  public MenuControllerAdvice menuControllerAdvice(Menu menu) {
    return new MenuControllerAdvice(menu);
  }

  @Bean
  public Set<Locale> availableLocales(@Value("${blossom.languages}") String[] languages) {
    return Stream.of(languages).sequential().map(language -> Locale.forLanguageTag(language)).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  @Bean
  public LocaleControllerAdvice languageControllerAdvice(Set<Locale> availableLocales) {
    return new LocaleControllerAdvice(availableLocales);
  }
}
