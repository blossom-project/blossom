package fr.blossom.ui.security;

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
    Object detailsObject = auth.getDetails();
    if (detailsObject instanceof WebAuthenticationDetails) {
      WebAuthenticationDetails details = (WebAuthenticationDetails) detailsObject;
      String identifier = (String) auth.getPrincipal();
      loginAttemptService.failAttempt(identifier, details.getRemoteAddress());
    }
  }
}
