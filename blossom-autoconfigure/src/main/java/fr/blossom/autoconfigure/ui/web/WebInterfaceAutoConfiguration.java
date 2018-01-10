package fr.blossom.autoconfigure.ui.web;

import fr.blossom.core.common.PluginConstants;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.search.SearchEngine;
import fr.blossom.core.common.utils.action_token.ActionTokenService;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.current_user.CurrentUserControllerAdvice;
import fr.blossom.ui.i18n.LocaleControllerAdvice;
import fr.blossom.ui.menu.Menu;
import fr.blossom.ui.menu.MenuControllerAdvice;
import fr.blossom.ui.web.ActivationController;
import fr.blossom.ui.web.ErrorControllerAdvice;
import fr.blossom.ui.web.HomeController;
import fr.blossom.ui.web.LoginController;
import fr.blossom.ui.web.OmnisearchController;
import fr.blossom.ui.web.StatusController;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.plugin.core.PluginRegistry;

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
  public StatusController statusController(HealthEndpoint healthEndpoint) {
    return new StatusController(healthEndpoint);
  }

  @Bean
  public OmnisearchController searchController(Client client,
    @Qualifier(PluginConstants.PLUGIN_SEARCH_ENGINE) PluginRegistry<SearchEngine, Class<? extends AbstractDTO>> registry) {
    return new OmnisearchController(client, registry);
  }

  @Bean
  public ActivationController activationController(ActionTokenService tokenService,
    UserService userService) {
    return new ActivationController(tokenService, userService);
  }

  @Bean
  public CurrentUserControllerAdvice currentUserControllerAdvice() {
    return new CurrentUserControllerAdvice();
  }

  @Bean
  public ErrorControllerAdvice errorControllerAdvice() {
    return new ErrorControllerAdvice();
  }

  @Bean
  public MenuControllerAdvice menuControllerAdvice(Menu menu) {
    return new MenuControllerAdvice(menu);
  }

  @Bean
  public LocaleControllerAdvice languageControllerAdvice(Set<Locale> availableLocales) {
    return new LocaleControllerAdvice(availableLocales);
  }
}
