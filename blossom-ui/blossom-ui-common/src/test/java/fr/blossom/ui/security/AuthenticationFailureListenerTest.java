package fr.blossom.ui.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFailureListenerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private LoginAttemptsService loginAttemptService;
  private AuthenticationFailureListener listener;

  @Before
  public void setUp() throws Exception {
    this.loginAttemptService = mock(LoginAttemptsService.class);
    this.listener = new AuthenticationFailureListener(this.loginAttemptService);
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
  @Ignore
  public void should_receive_event() throws Exception {
    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("identifier");

    AuthenticationFailureBadCredentialsEvent event = mock(AuthenticationFailureBadCredentialsEvent.class);
    when(event.getAuthentication()).thenReturn(authentication);

    this.listener.onApplicationEvent(event);
  }

}
