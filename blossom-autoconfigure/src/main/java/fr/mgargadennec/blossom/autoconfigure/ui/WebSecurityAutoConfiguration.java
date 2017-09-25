package fr.mgargadennec.blossom.autoconfigure.ui;

import fr.mgargadennec.blossom.core.association_user_role.AssociationUserRoleService;
import fr.mgargadennec.blossom.core.common.PluginConstants;
import fr.mgargadennec.blossom.core.common.utils.privilege.PrivilegePlugin;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.LastConnectionUpdateAuthenticationSuccessHandlerImpl;
import fr.mgargadennec.blossom.ui.current_user.CurrentUserDetailsServiceImpl;
import fr.mgargadennec.blossom.ui.current_user.SystemUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnWebApplication
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@PropertySource("classpath:/security.properties")
@EnableConfigurationProperties(DefaultAccountProperties.class)
public class WebSecurityAutoConfiguration {

  private static final String BLOSSOM_BASE_PATH = "blossom";
  private static final String BLOSSOM_API_BASE_PATH = BLOSSOM_BASE_PATH + "/api";
  private static final String BLOSSOM_REMEMBER_ME_COOKIE_NAME = "blossom";

  @Bean
  public UserDetailsService dbUserDetailsService(UserService userService,
    AssociationUserRoleService associationUserRoleService) {
    return new CurrentUserDetailsServiceImpl(userService, associationUserRoleService);
  }

  @Bean
  public UserDetailsService systemUserDetailsService(
    @Qualifier(PluginConstants.PLUGIN_PRIVILEGES)
      PluginRegistry<PrivilegePlugin, String> privilegeRegistry,
    DefaultAccountProperties properties) {
    if (properties.isEnabled()) {
      return new SystemUserDetailsServiceImpl(privilegeRegistry, properties.getIdentifier(),
        properties.getPassword());
    }
    return null;
  }

  @Bean
  public LastConnectionUpdateAuthenticationSuccessHandlerImpl lastConnectionUpdateAuthenticationSuccessHandler(
    UserService userService) {
    return new LastConnectionUpdateAuthenticationSuccessHandlerImpl(userService);
  }

  @Configuration
  @Order(-1)
  public static class GlobalSecurityConfigurerAdapter extends
    GlobalAuthenticationConfigurerAdapter {

    @Autowired
    @Qualifier(value = "dbUserDetailsService")
    public UserDetailsService dbUserDetailsService;

    @Autowired(required = false)
    @Qualifier(value = "systemUserDetailsService")
    public UserDetailsService systemUserDetailsService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
      if (systemUserDetailsService != null) {
        auth.userDetailsService(systemUserDetailsService);
      }
      auth
        .userDetailsService(dbUserDetailsService).passwordEncoder(passwordEncoder);
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
        .exceptionHandling();
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
