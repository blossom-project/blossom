package fr.blossom.ui.security;

import fr.blossom.ui.current_user.CurrentUser;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationSuccessListener
  implements ApplicationListener<AuthenticationSuccessEvent> {

  private final LoginAttemptsService loginAttemptService;

  public AuthenticationSuccessListener(LoginAttemptsService loginAttemptService) {
    this.loginAttemptService = loginAttemptService;
  }

  public void onApplicationEvent(AuthenticationSuccessEvent e) {
    Authentication auth = e.getAuthentication();
    if (auth.getPrincipal() instanceof CurrentUser) {
      String identifier = ((CurrentUser) auth.getPrincipal()).getUsername();
      if(auth.getDetails() instanceof  WebAuthenticationDetails) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
        loginAttemptService.successfulAttempt(identifier, details.getRemoteAddress());
      }
    }
  }
}
