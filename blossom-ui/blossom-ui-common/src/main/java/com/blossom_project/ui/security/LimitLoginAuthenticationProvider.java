package com.blossom_project.ui.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

  private final LoginAttemptsService userLoginAttemptsService;

  public LimitLoginAuthenticationProvider(UserDetailsService userDetailsService,
    LoginAttemptsService userLoginAttemptsService) {
    super.setUserDetailsService(userDetailsService);
    this.userLoginAttemptsService = userLoginAttemptsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Object detailsObject = authentication.getDetails();
    if(detailsObject instanceof  WebAuthenticationDetails){
      final WebAuthenticationDetails details = (WebAuthenticationDetails) detailsObject;

      if (userLoginAttemptsService.isBlocked(authentication.getName(), details.getRemoteAddress())) {
        throw new LockedException(messages.getMessage(
          "AbstractUserDetailsAuthenticationProvider.locked",
          "User account is locked"));
      }

      try {
        Authentication auth = super.authenticate(authentication);
        userLoginAttemptsService
          .successfulAttempt(authentication.getName(), details.getRemoteAddress());
        return auth;
      } catch (BadCredentialsException e) {
        userLoginAttemptsService.failAttempt(authentication.getName(), details.getRemoteAddress());
        throw e;
      }
    }
    return super.authenticate(authentication);
  }
}
