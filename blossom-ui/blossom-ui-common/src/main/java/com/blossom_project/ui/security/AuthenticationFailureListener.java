package com.blossom_project.ui.security;

import com.google.common.base.Preconditions;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationFailureListener
  implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  private final LoginAttemptsService loginAttemptService;

  public AuthenticationFailureListener(LoginAttemptsService loginAttemptService) {
    Preconditions.checkArgument(loginAttemptService != null);
    this.loginAttemptService = loginAttemptService;
  }

  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
    Authentication auth = e.getAuthentication();
    if (auth.getPrincipal() instanceof String) {
      String identifier = (String) auth.getPrincipal();
      if(auth.getDetails() instanceof  WebAuthenticationDetails) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
        loginAttemptService.failAttempt(identifier, details.getRemoteAddress());
      }
    }
  }
}
