package fr.blossom.ui.security;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.blossom.ui.current_user.CurrentUserBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationSuccessListenerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private LoginAttemptsService loginAttemptService;
  private AuthenticationSuccessListener listener;

  @Before
  public void setUp() throws Exception {
    this.loginAttemptService = mock(LoginAttemptsService.class);
    this.listener = new AuthenticationSuccessListener(this.loginAttemptService);
  }

  @Test
  public void should_succeed_on_null_login_attempt_service() throws Exception {
  }

  @Test
  public void should_fail_on_null_login_attempt_service() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    new AuthenticationFailureListener(null);
  }

  @Test
  public void should_receive_event() throws Exception {
    String principal = "principal";
    String credential = "password";
    String remoteAddress = "remoteAddress";
    WebAuthenticationDetails details = mock(WebAuthenticationDetails.class);
    when(details.getSessionId()).thenReturn("sessionId");
    when(details.getRemoteAddress()).thenReturn(remoteAddress);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(new CurrentUserBuilder().identifier(principal).passwordHash(credential).addPrivilege("testPrivilege").toCurrentUser());
    when(authentication.getDetails()).thenReturn(details);

    AuthenticationSuccessEvent event = mock(AuthenticationSuccessEvent.class);
    when(event.getAuthentication()).thenReturn(authentication);

    this.listener.onApplicationEvent(event);

    verify(this.loginAttemptService, times(1)).successfulAttempt(eq(principal), eq(remoteAddress));
  }

}
