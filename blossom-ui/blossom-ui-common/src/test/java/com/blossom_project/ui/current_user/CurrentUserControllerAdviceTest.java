package com.blossom_project.ui.current_user;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class CurrentUserControllerAdviceTest {

  @Test
  public void should_get_current_user_null_authentication() throws Exception {
    assertNull(new CurrentUserControllerAdvice().getCurrentUser(null));
  }

  @Test
  public void should_get_current_user_wrong_class_principal() throws Exception {
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn("principalString");

    assertNull(new CurrentUserControllerAdvice().getCurrentUser(authentication));
  }

  @Test
  public void should_get_current_user() throws Exception {
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(new CurrentUserBuilder().identifier("test").passwordHash("test").addPrivilege("test").toCurrentUser());
    assertNotNull(new CurrentUserControllerAdvice().getCurrentUser(authentication));
  }
}
