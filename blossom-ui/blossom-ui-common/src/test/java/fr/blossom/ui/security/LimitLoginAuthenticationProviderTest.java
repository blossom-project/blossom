package fr.blossom.ui.security;

import static org.mockito.Matchers.anyString;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@RunWith(MockitoJUnitRunner.class)
public class LimitLoginAuthenticationProviderTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private UserDetailsService userDetailsService;
  private LoginAttemptsService loginAttemptsService;
  private LimitLoginAuthenticationProvider authProvider;

  @Before
  public void setUp() throws Exception {
    this.userDetailsService = mock(UserDetailsService.class);
    this.loginAttemptsService = mock(LoginAttemptsService.class);
    this.authProvider = new LimitLoginAuthenticationProvider(this.userDetailsService,
      this.loginAttemptsService);
  }

  @Test
  public void should_be_blocked() {
    thrown.expect(LockedException.class);

    String principal = "username";
    String credential = "password";
    String sessionId = "sessionId";
    String remoteAddress = "remoteAddress";

    WebAuthenticationDetails details = mock(WebAuthenticationDetails.class);
    when(details.getSessionId()).thenReturn(sessionId);
    when(details.getRemoteAddress()).thenReturn(remoteAddress);

    UsernamePasswordAuthenticationToken authentication = mock(
      UsernamePasswordAuthenticationToken.class);
    when(authentication.getName()).thenReturn(principal);
    when(authentication.getPrincipal()).thenReturn(principal);
    when(authentication.getCredentials()).thenReturn(credential);
    when(authentication.getDetails()).thenReturn(details);

    when(loginAttemptsService.isBlocked(anyString(), anyString())).thenReturn(true);
    this.authProvider.authenticate(authentication);
  }

  @Test
  public void should_initialize() throws Exception {
    String principal = "username";
    String credential = "password";
    String sessionId = "sessionId";
    String remoteAddress = "remoteAddress";

    WebAuthenticationDetails details = mock(WebAuthenticationDetails.class);
    when(details.getSessionId()).thenReturn(sessionId);
    when(details.getRemoteAddress()).thenReturn(remoteAddress);

    UsernamePasswordAuthenticationToken authentication = mock(
      UsernamePasswordAuthenticationToken.class);
    when(authentication.getName()).thenReturn(principal);
    when(authentication.getPrincipal()).thenReturn(principal);
    when(authentication.getCredentials()).thenReturn(credential);
    when(authentication.getDetails()).thenReturn(details);

    when(this.userDetailsService.loadUserByUsername(eq(principal))).thenReturn(
      new CurrentUserBuilder().identifier(principal).passwordHash(credential).toCurrentUser());

    this.authProvider.authenticate(authentication);
  }


  @Test
  public void should_good_credentials() throws Exception {
    String principal = "username";
    String credential = "password";
    String sessionId = "sessionId";
    String remoteAddress = "remoteAddress";

    WebAuthenticationDetails details = mock(WebAuthenticationDetails.class);
    when(details.getSessionId()).thenReturn(sessionId);
    when(details.getRemoteAddress()).thenReturn(remoteAddress);

    UsernamePasswordAuthenticationToken authentication = mock(
      UsernamePasswordAuthenticationToken.class);
    when(authentication.getName()).thenReturn(principal);
    when(authentication.getPrincipal()).thenReturn(principal);
    when(authentication.getCredentials()).thenReturn(credential);
    when(authentication.getDetails()).thenReturn(details);

    when(this.userDetailsService.loadUserByUsername(eq(principal))).thenReturn(
      new CurrentUserBuilder().identifier(principal).passwordHash(credential).toCurrentUser());

    this.authProvider.authenticate(authentication);
    verify(this.loginAttemptsService, times(1)).successfulAttempt(eq(principal), eq(remoteAddress));
  }

  @Test
  public void should_bad_credentials() throws Exception {
    thrown.expect(BadCredentialsException.class);

    String principal = "username";
    String credential = "password";
    String sessionId = "sessionId";
    String remoteAddress = "remoteAddress";

    WebAuthenticationDetails details = mock(WebAuthenticationDetails.class);
    when(details.getSessionId()).thenReturn(sessionId);
    when(details.getRemoteAddress()).thenReturn(remoteAddress);

    UsernamePasswordAuthenticationToken authentication = mock(
      UsernamePasswordAuthenticationToken.class);
    when(authentication.getName()).thenReturn(principal);
    when(authentication.getPrincipal()).thenReturn(principal);
    when(authentication.getCredentials()).thenReturn("bad_credential");
    when(authentication.getDetails()).thenReturn(details);

    when(this.userDetailsService.loadUserByUsername(eq(principal))).thenReturn(
      new CurrentUserBuilder().identifier(principal).passwordHash(credential).toCurrentUser());

    try {
      this.authProvider.authenticate(authentication);
    } catch (BadCredentialsException e) {
      verify(this.loginAttemptsService, times(1)).failAttempt(eq(principal), eq(remoteAddress));
      throw e;
    }
  }
}
