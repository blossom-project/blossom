package fr.blossom.autoconfigure.ui.web;

import static fr.blossom.autoconfigure.ui.WebContextAutoConfiguration.BLOSSOM_BASE_PATH;
import static fr.blossom.autoconfigure.ui.WebSecurityAutoConfiguration.BLOSSOM_REMEMBER_ME_COOKIE_NAME;

import fr.blossom.core.common.PluginConstants;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.search.SearchEngine;
import fr.blossom.core.common.utils.action_token.ActionTokenService;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.LastConnectionUpdateAuthenticationSuccessHandlerImpl;
import fr.blossom.ui.current_user.CurrentUserControllerAdvice;
import fr.blossom.ui.i18n.LocaleControllerAdvice;
import fr.blossom.ui.menu.Menu;
import fr.blossom.ui.menu.MenuControllerAdvice;
import fr.blossom.ui.security.LimitLoginAuthenticationProvider;
import fr.blossom.ui.web.ActivationController;
import fr.blossom.ui.web.error.BlossomErrorViewResolver;
import fr.blossom.ui.web.error.ErrorControllerAdvice;
import fr.blossom.ui.web.HomeController;
import fr.blossom.ui.web.LoginController;
import fr.blossom.ui.web.OmnisearchController;
import fr.blossom.ui.web.StatusController;
import java.util.Locale;
import java.util.Set;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(HomeController.class)
public class WebInterfaceAutoConfiguration {

  @Configuration
  static class BlossomErrorViewResolverConfiguration {

    private final ApplicationContext applicationContext;
    private final ResourceProperties resourceProperties;

    BlossomErrorViewResolverConfiguration(ApplicationContext applicationContext,
      ResourceProperties resourceProperties) {
      this.applicationContext = applicationContext;
      this.resourceProperties = resourceProperties;
    }

    @Bean
    public BlossomErrorViewResolver blossomErrorViewResolver() {
      return new BlossomErrorViewResolver(this.applicationContext, this.resourceProperties);
    }
  }

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

  @Configuration
  public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final LastConnectionUpdateAuthenticationSuccessHandlerImpl lastConnectionUpdateAuthenticationSuccessHandler;
    private final SessionRegistry sessionRegistry;
    public final LimitLoginAuthenticationProvider limitLoginAuthenticationProvider;


    public FormLoginWebSecurityConfigurerAdapter(
      LastConnectionUpdateAuthenticationSuccessHandlerImpl lastConnectionUpdateAuthenticationSuccessHandler,
      SessionRegistry sessionRegistry,
      LimitLoginAuthenticationProvider limitLoginAuthenticationProvider) {
      this.lastConnectionUpdateAuthenticationSuccessHandler = lastConnectionUpdateAuthenticationSuccessHandler;
      this.sessionRegistry = sessionRegistry;
      this.limitLoginAuthenticationProvider = limitLoginAuthenticationProvider;
    }

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
      return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(limitLoginAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/" + BLOSSOM_BASE_PATH + "/**")
        .authorizeRequests().anyRequest().fullyAuthenticated()
        .and().formLogin().loginPage("/" + BLOSSOM_BASE_PATH + "/login").failureUrl("/" + BLOSSOM_BASE_PATH + "/login?error").successHandler(lastConnectionUpdateAuthenticationSuccessHandler).permitAll()
        .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/" + BLOSSOM_BASE_PATH + "/logout")).deleteCookies(BLOSSOM_REMEMBER_ME_COOKIE_NAME).logoutSuccessUrl("/" + BLOSSOM_BASE_PATH + "/login").permitAll()
        .and().rememberMe().rememberMeCookieName(BLOSSOM_REMEMBER_ME_COOKIE_NAME)
        .and().exceptionHandling()
        .and().sessionManagement().maximumSessions(10).maxSessionsPreventsLogin(true).expiredUrl("/" + BLOSSOM_BASE_PATH + "/login").sessionRegistry(sessionRegistry);
    }
  }
}
