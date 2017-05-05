package fr.mgargadennec.blossom.autoconfigure.ui;

import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.LastConnectionUpdateAuthenticationSuccessHandlerImpl;
import fr.mgargadennec.blossom.ui.current_user.CurrentUserDetailsServiceImpl;
import fr.mgargadennec.blossom.ui.current_user.SystemUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
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
public class WebSecurityAutoConfiguration {
  private final static String BLOSSOM_BASE_PATH = "blossom";
  private final static String BLOSSOM_API_BASE_PATH = BLOSSOM_BASE_PATH + "/api";

  @Bean
  public UserDetailsService dbUserDetailsService(UserService userService) {
    return new CurrentUserDetailsServiceImpl(userService);
  }

  @Bean
  public UserDetailsService systemUserDetailsService() {
    return new SystemUserDetailsServiceImpl();
  }

  @Bean
  public LastConnectionUpdateAuthenticationSuccessHandlerImpl lastConnectionUpdateAuthenticationSuccessHandler(UserService userService) {
    return new LastConnectionUpdateAuthenticationSuccessHandlerImpl(userService);
  }

  @Configuration
  @Order(-1)
  public static class GlobalSecurityConfigurerAdapter extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    @Qualifier(value = "dbUserDetailsService")
    public UserDetailsService dbUserDetailsService;

    @Autowired
    @Qualifier(value = "systemUserDetailsService")
    public UserDetailsService systemUserDetailsService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
      auth
        .userDetailsService(systemUserDetailsService).and()
        .userDetailsService(dbUserDetailsService).passwordEncoder(passwordEncoder);
    }
  }


  @Configuration
  @Order(1)
  public static class PublicWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/public/**").csrf().disable().authorizeRequests().anyRequest().permitAll();
    }
  }

  @Configuration
  @Order(2)
  public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/" + BLOSSOM_API_BASE_PATH + "/**")
        .csrf().disable()
        .authorizeRequests().anyRequest().fullyAuthenticated().and()
        .httpBasic().and()
        .exceptionHandling().and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
  }

  @Configuration
  @Order(3)
  public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Autowired
    private LastConnectionUpdateAuthenticationSuccessHandlerImpl lastConnectionUpdateAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/" + BLOSSOM_BASE_PATH + "/**")
        .authorizeRequests()
        .anyRequest().fullyAuthenticated().and()
        .formLogin().loginPage("/" + BLOSSOM_BASE_PATH + "/login").failureUrl("/" + BLOSSOM_BASE_PATH + "/login?error").successHandler(lastConnectionUpdateAuthenticationSuccessHandler).permitAll().and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/" + BLOSSOM_BASE_PATH + "/logout")).deleteCookies("blossom").logoutSuccessUrl("/" + BLOSSOM_BASE_PATH + "/login").permitAll().and()
        .rememberMe().rememberMeCookieName("blossom").and()
        .exceptionHandling();

    }
  }


  @Configuration
  @Order(4)
  public static class AppWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/**")
        .csrf().disable()
        .authorizeRequests().anyRequest().permitAll();
    }
  }
}
