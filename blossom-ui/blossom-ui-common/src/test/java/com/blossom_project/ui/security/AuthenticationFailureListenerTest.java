package com.blossom_project.ui.security;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

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
  public void should_receive_event() throws Exception {
    String principal = "principal";
    String remoteAddress = "remoteAddress";
    WebAuthenticationDetails details = mock(WebAuthenticationDetails.class);
    when(details.getRemoteAddress()).thenReturn(remoteAddress);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(principal);
    when(authentication.getDetails()).thenReturn(details);

    AuthenticationFailureBadCredentialsEvent event = mock(
      AuthenticationFailureBadCredentialsEvent.class);
    when(event.getAuthentication()).thenReturn(authentication);

    this.listener.onApplicationEvent(event);

    verify(this.loginAttemptService, times(1)).failAttempt(eq(principal), eq(remoteAddress));
  }

}
