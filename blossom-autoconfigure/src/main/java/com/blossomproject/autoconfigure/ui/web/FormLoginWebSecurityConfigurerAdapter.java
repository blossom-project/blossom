package com.blossomproject.autoconfigure.ui.web;

import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.ui.BlossomAuthenticationSuccessHandlerImpl;
import com.blossomproject.ui.security.LimitLoginAuthenticationProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import static com.blossomproject.autoconfigure.ui.WebContextAutoConfiguration.BLOSSOM_BASE_PATH;
import static com.blossomproject.autoconfigure.ui.WebSecurityAutoConfiguration.BLOSSOM_REMEMBER_ME_COOKIE_NAME;

@ConditionalOnBean(WebInterfaceAutoConfiguration.class)
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Configuration
public class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final BlossomAuthenticationSuccessHandlerImpl blossomAuthenticationSuccessHandler;
  private final SessionRegistry sessionRegistry;
  private final Privilege switchUserPrivilege;
  private final LimitLoginAuthenticationProvider limitLoginAuthenticationProvider;
  private final BlossomWebBackOfficeProperties webBackOfficeProperties;


  public FormLoginWebSecurityConfigurerAdapter(
    UserDetailsService userDetailsService,
    BlossomAuthenticationSuccessHandlerImpl blossomAuthenticationSuccessHandler,
    SessionRegistry sessionRegistry,
    LimitLoginAuthenticationProvider limitLoginAuthenticationProvider,
    @Qualifier("switchUserPrivilege") Privilege switchUserPrivilege,
    BlossomWebBackOfficeProperties webBackOfficeProperties) {
    this.userDetailsService = userDetailsService;
    this.blossomAuthenticationSuccessHandler = blossomAuthenticationSuccessHandler;
    this.sessionRegistry = sessionRegistry;
    this.limitLoginAuthenticationProvider = limitLoginAuthenticationProvider;
    this.switchUserPrivilege = switchUserPrivilege;
    this.webBackOfficeProperties = webBackOfficeProperties;
  }

  @Bean
  public static ServletListenerRegistrationBean httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
  }

  @Bean
  public SwitchUserFilter switchUserProcessingFilter() {
    SwitchUserFilter filter = new SwitchUserFilter();
    filter.setUserDetailsService(userDetailsService);
    filter.setSwitchAuthorityRole(switchUserPrivilege.privilege());
    filter.setSwitchUserUrl("/" + BLOSSOM_BASE_PATH + "/administration/_impersonate");
    filter.setExitUserUrl("/" + BLOSSOM_BASE_PATH + "/administration/_impersonate/logout");
    filter.setTargetUrl("/" + BLOSSOM_BASE_PATH);
    filter.setSwitchFailureUrl("/" + BLOSSOM_BASE_PATH);
    return filter;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(limitLoginAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterAfter(switchUserProcessingFilter(), FilterSecurityInterceptor.class);

    http.antMatcher("/" + BLOSSOM_BASE_PATH + "/**")
      .authorizeRequests().anyRequest().fullyAuthenticated()
      .and().formLogin().loginPage("/" + BLOSSOM_BASE_PATH + "/login")
      .failureUrl("/" + BLOSSOM_BASE_PATH + "/login?error")
      .successHandler(blossomAuthenticationSuccessHandler).permitAll()
      .and().logout()
      .logoutRequestMatcher(new AntPathRequestMatcher("/" + BLOSSOM_BASE_PATH + "/logout"))
      .deleteCookies(BLOSSOM_REMEMBER_ME_COOKIE_NAME)
      .logoutSuccessUrl("/" + BLOSSOM_BASE_PATH + "/login").permitAll()
      .and().rememberMe().rememberMeCookieName(BLOSSOM_REMEMBER_ME_COOKIE_NAME)
      .and().exceptionHandling().defaultAuthenticationEntryPointFor(
      (request, response, authException) -> response.sendError(401),
      new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"))
      .and().sessionManagement()
      .maximumSessions(webBackOfficeProperties.getMaxSessionsPerUser()).maxSessionsPreventsLogin(true)
      .expiredSessionStrategy(
        new WebInterfaceAutoConfiguration.BlossomInvalidSessionStrategy("/" + BLOSSOM_BASE_PATH + "/login"))
      .sessionRegistry(sessionRegistry);

  }
}