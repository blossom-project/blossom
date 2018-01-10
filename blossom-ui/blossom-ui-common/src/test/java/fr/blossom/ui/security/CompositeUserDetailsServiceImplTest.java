package fr.blossom.ui.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.blossom.ui.current_user.CurrentUserBuilder;
import fr.blossom.ui.current_user.UserDTOBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CompositeUserDetailsServiceImplTest {

  private CompositeUserDetailsServiceImpl compositeUserDetailsService;
  private UserDetailsService mock1;
  private UserDetailsService mock2;
  private String user1;
  private String user2;

  @Before
  public void setUp() throws Exception {
    this.user1 = "user1";
    this.user2 = "user2";
    this.mock1 = mock(UserDetailsService.class);
    when(mock1.loadUserByUsername(eq(user1)))
      .thenReturn(new CurrentUserBuilder().identifier(user1).passwordHash("hash1").user(
        new UserDTOBuilder().identifier(user1).passwordHash("hash1").toUserDTO())
        .toCurrentUser());
    when(mock1.loadUserByUsername(not(eq(user1)))).thenThrow(UsernameNotFoundException.class);

    this.mock2 = mock(UserDetailsService.class);
    when(mock2.loadUserByUsername(eq(user2)))
      .thenReturn(new CurrentUserBuilder().identifier(user2).passwordHash("hash2").user(
        new UserDTOBuilder().identifier(user2).passwordHash("hash2").toUserDTO())
        .toCurrentUser());
    when(mock2.loadUserByUsername(not(eq(user2)))).thenThrow(UsernameNotFoundException.class);

    this.compositeUserDetailsService = spy(new CompositeUserDetailsServiceImpl(mock1, mock2));
  }

  @Test(expected = UsernameNotFoundException.class)
  public void loadSystemUserByUsername_should_throw_exception_when_no_user_found() {
    String username = "anyString";
    try {
      this.compositeUserDetailsService.loadUserByUsername(username);
    } catch (UsernameNotFoundException e) {
      verify(this.mock1, times(1)).loadUserByUsername(eq(username));
      verify(this.mock2, times(1)).loadUserByUsername(eq(username));
      throw e;
    }
  }

  @Test
  public void loadSystemUserUsername_should_return_user_from_first_mock() {
    String username = "user1";
    UserDetails currentUser = this.compositeUserDetailsService.loadUserByUsername(username);
    assertNotNull(currentUser);
    assertTrue(currentUser.getUsername().equals(username));

    verify(this.mock1, times(1)).loadUserByUsername(eq(username));
    verify(this.mock2, times(0)).loadUserByUsername(eq(username));
  }

  @Test
  public void loadSystemUserUsername_should_return_user_from_second_mock() {
    String username = "user2";
    UserDetails currentUser = this.compositeUserDetailsService.loadUserByUsername(username);
    assertNotNull(currentUser);
    assertTrue(currentUser.getUsername().equals(username));

    verify(this.mock1, times(1)).loadUserByUsername(eq(username));
    verify(this.mock2, times(1)).loadUserByUsername(eq(username));
  }

}
