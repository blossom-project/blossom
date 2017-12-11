package fr.blossom.autoconfigure.ui;

import fr.blossom.core.association_user_role.AssociationUserRoleService;
import fr.blossom.core.common.PluginConstants;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.LastConnectionUpdateAuthenticationSuccessHandlerImpl;
import fr.blossom.ui.current_user.CurrentUserDetailsServiceImpl;
import fr.blossom.ui.current_user.SystemUserDetailsServiceImpl;
import fr.blossom.ui.security.AuthenticationFailureListener;
import fr.blossom.ui.security.AuthenticationSuccessListener;
import fr.blossom.ui.security.CompositeUserDetailsServiceImpl;
import fr.blossom.ui.security.LimitLoginAuthenticationProvider;
import fr.blossom.ui.security.LoginAttemptServiceImpl;
import fr.blossom.ui.security.LoginAttemptsService;
import fr.blossom.ui.web.utils.session.BlossomSessionRegistryImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnWebApplication
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@PropertySource("classpath:/security.properties")
@EnableConfigurationProperties(DefaultAccountProperties.class)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityAutoConfiguration {

  private static final String BLOSSOM_BASE_PATH = "blossom";
  private static final String BLOSSOM_API_BASE_PATH = BLOSSOM_BASE_PATH + "/api";
  private static final String BLOSSOM_REMEMBER_ME_COOKIE_NAME = "blossom";

  @Bean
  public LoginAttemptsService loginAttemptsService() {
    return new LoginAttemptServiceImpl(10);
  }

  @Bean
  public AuthenticationFailureListener authenticationFailureListener(
    LoginAttemptsService loginAttemptService) {
    return new AuthenticationFailureListener(loginAttemptService);
  }

  @Bean
  public AuthenticationSuccessListener authenticationSuccessListener(
    LoginAttemptsService loginAttemptService) {
    return new AuthenticationSuccessListener(loginAttemptService);
  }

  @Bean
  public UserDetailsService dbUserDetailsService(UserService userService,
    AssociationUserRoleService associationUserRoleService) {
    return new CurrentUserDetailsServiceImpl(userService, associationUserRoleService);
  }

  @Bean
  public UserDetailsService systemUserDetailsService(@Qualifier(PluginConstants.PLUGIN_PRIVILEGES) PluginRegistry<Privilege, String> privilegeRegistry,
    DefaultAccountProperties properties, PasswordEncoder passwordEncoder) {
    if (properties.isEnabled()) {
      return new SystemUserDetailsServiceImpl(privilegeRegistry, properties.getIdentifier(), passwordEncoder.encode(properties.getPassword()));
    }
    return null;
  }

  @Bean
  public UserDetailsService compositeUserDetailsService(List<UserDetailsService> userDetailsServices) {
    return new CompositeUserDetailsServiceImpl(userDetailsServices.toArray(new UserDetailsService[userDetailsServices.size()]));
  }

  @Bean
  public LastConnectionUpdateAuthenticationSuccessHandlerImpl lastConnectionUpdateAuthenticationSuccessHandler(
    UserService userService) {
    return new LastConnectionUpdateAuthenticationSuccessHandlerImpl(userService);
  }

  @Bean
  public LimitLoginAuthenticationProvider limitLoginAuthenticationProvider(
    @Qualifier(value = "compositeUserDetailsService") UserDetailsService compositeUserDetailsService,
    PasswordEncoder passwordEncoder,
    LoginAttemptsService loginAttempsService) {

    LimitLoginAuthenticationProvider provider = new LimitLoginAuthenticationProvider(
      compositeUserDetailsService,
      loginAttempsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Configuration
  @Order(-1)
  public static class GlobalSecurityConfigurerAdapter extends
    GlobalAuthenticationConfigurerAdapter {

    @Autowired
    public LimitLoginAuthenticationProvider limitLoginAuthenticationProvider;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(limitLoginAuthenticationProvider);
    }

  }

  @Configuration
  @Order(1)
  public static class PublicWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/public/**").csrf().disable().authorizeRequests().anyRequest().permitAll();
      http.antMatcher("/" + BLOSSOM_BASE_PATH + "/public/**").authorizeRequests().anyRequest()
        .permitAll();
    }
  }

  @Configuration
  @Order(2)
  public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/" + BLOSSOM_API_BASE_PATH + "/**").csrf().disable().authorizeRequests()
        .anyRequest()
        .fullyAuthenticated().and().httpBasic().and().exceptionHandling().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
  }

  @Configuration
  @Order(3)
  public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    private LastConnectionUpdateAuthenticationSuccessHandlerImpl lastConnectionUpdateAuthenticationSuccessHandler;
    @Autowired
    private AssociationUserRoleService associationUserRoleService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/" + BLOSSOM_BASE_PATH + "/**").authorizeRequests().anyRequest()
        .fullyAuthenticated().and()
        .formLogin().loginPage("/" + BLOSSOM_BASE_PATH + "/login")
        .failureUrl("/" + BLOSSOM_BASE_PATH + "/login?error")
        .successHandler(lastConnectionUpdateAuthenticationSuccessHandler).permitAll().and().logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/" + BLOSSOM_BASE_PATH + "/logout"))
        .deleteCookies(BLOSSOM_REMEMBER_ME_COOKIE_NAME)
        .logoutSuccessUrl("/" + BLOSSOM_BASE_PATH + "/login")
        .permitAll().and().rememberMe().rememberMeCookieName(BLOSSOM_REMEMBER_ME_COOKIE_NAME).and()
        .exceptionHandling().and()
        .sessionManagement().maximumSessions(10)
        .maxSessionsPreventsLogin(true)
        .expiredUrl("/" + BLOSSOM_BASE_PATH + "/login")
        .sessionRegistry(blossomSessionRegistry());
    }


    @Bean
    public SessionRegistry blossomSessionRegistry() {
      return new BlossomSessionRegistryImpl(associationUserRoleService);
    }

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
      return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }


  }

  @Configuration
  @Order(4)
  public static class AppWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/**").csrf().disable().authorizeRequests().anyRequest().permitAll();
    }
  }
}
