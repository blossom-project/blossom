package com.blossomproject.autoconfigure.ui.web;

import static com.blossomproject.autoconfigure.ui.WebContextAutoConfiguration.BLOSSOM_BASE_PATH;
import static com.blossomproject.autoconfigure.ui.WebSecurityAutoConfiguration.BLOSSOM_REMEMBER_ME_COOKIE_NAME;

import com.blossomproject.autoconfigure.ui.WebContextAutoConfiguration;
import com.blossomproject.core.association_user_role.AssociationUserRoleService;
import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngine;
import com.blossomproject.core.common.utils.action_token.ActionTokenService;
import com.blossomproject.core.user.UserService;
import com.blossomproject.ui.LastConnectionUpdateAuthenticationSuccessHandlerImpl;
import com.blossomproject.ui.current_user.CurrentUserControllerAdvice;
import com.blossomproject.ui.i18n.LocaleControllerAdvice;
import com.blossomproject.ui.menu.Menu;
import com.blossomproject.ui.menu.MenuControllerAdvice;
import com.blossomproject.ui.security.LimitLoginAuthenticationProvider;
import com.blossomproject.ui.web.ActivationController;
import com.blossomproject.ui.web.HomeController;
import com.blossomproject.ui.web.LoginController;
import com.blossomproject.ui.web.OmnisearchController;
import com.blossomproject.ui.web.StatusController;
import com.blossomproject.ui.web.error.BlossomErrorViewResolver;
import com.blossomproject.ui.web.error.ErrorControllerAdvice;
import com.blossomproject.ui.web.utils.session.BlossomSessionRegistryImpl;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.util.Assert;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(HomeController.class)
@AutoConfigureAfter(WebContextAutoConfiguration.class)
public class WebInterfaceAutoConfiguration {
  private final AssociationUserRoleService associationUserRoleService;

  public WebInterfaceAutoConfiguration(
    AssociationUserRoleService associationUserRoleService) {
    this.associationUserRoleService = associationUserRoleService;
  }

  @Bean
  public SessionRegistry blossomSessionRegistry() {
    return new BlossomSessionRegistryImpl(associationUserRoleService);
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
  static class BlossomErrorViewResolverConfiguration {

    private final ApplicationContext applicationContext;
    private final ResourceProperties resourceProperties;

    BlossomErrorViewResolverConfiguration(ApplicationContext applicationContext,
      ResourceProperties resourceProperties,
      AssociationUserRoleService associationUserRoleService) {
      this.applicationContext = applicationContext;
      this.resourceProperties = resourceProperties;
    }

    @Bean
    public BlossomErrorViewResolver blossomErrorViewResolver() {
      return new BlossomErrorViewResolver(this.applicationContext, this.resourceProperties);
    }
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
        .and().exceptionHandling().defaultAuthenticationEntryPointFor((request, response, authException) -> response.sendError(401), new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"))
        .and().sessionManagement()
        .maximumSessions(10).maxSessionsPreventsLogin(true)
        .expiredSessionStrategy(new BlossomInvalidSessionStrategy("/" + BLOSSOM_BASE_PATH + "/login"))
        .sessionRegistry(sessionRegistry);
    }
  }

  public static class BlossomInvalidSessionStrategy implements SessionInformationExpiredStrategy{
    private final Logger logger = LoggerFactory.getLogger(BlossomInvalidSessionStrategy.class);
    private final String destinationUrl;
    private final RedirectStrategy redirectStrategy;

    public BlossomInvalidSessionStrategy(String invalidSessionUrl) {
      this(invalidSessionUrl, new DefaultRedirectStrategy());
    }

    public BlossomInvalidSessionStrategy(String invalidSessionUrl, RedirectStrategy redirectStrategy) {
      Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl),
        "url must start with '/' or with 'http(s)'");
      this.destinationUrl=invalidSessionUrl;
      this.redirectStrategy=redirectStrategy;
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
      throws IOException, ServletException {
      if(logger.isDebugEnabled()){
        logger.debug("Redirecting to '" + destinationUrl + "'");
      }
      String ajaxHeader = event.getRequest().getHeader("X-Requested-With");

      if (ajaxHeader!=null && "XMLHttpRequest".equals(ajaxHeader)){
        logger.info("Ajax call detected, send {} error code", 401);
        event.getResponse().sendError(401);
        return;
      }

      redirectStrategy.sendRedirect(event.getRequest(), event.getResponse(), destinationUrl);
    }
  }
}
