package fr.blossom.ui.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationFailureListener
  implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  private final LoginAttemptsService loginAttemptService;

  public AuthenticationFailureListener(LoginAttemptsService loginAttemptService) {
    this.loginAttemptService = loginAttemptService;
  }

  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
    Authentication auth = e.getAuthentication();
    String identifier = (String) auth.getPrincipal();
    WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();

    loginAttemptService.failAttempt(identifier, details.getRemoteAddress());
  }
}
