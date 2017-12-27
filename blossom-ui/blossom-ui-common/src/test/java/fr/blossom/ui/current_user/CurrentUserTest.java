package fr.blossom.ui.current_user;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CurrentUserTest {

  @Test
  public void should_have_privilege() throws Exception {
    String privilege = "privilege";
    CurrentUser currentUser = new CurrentUserBuilder().identifier("test").passwordHash("test")
      .addPrivilege(privilege).toCurrentUser();
    assertTrue(currentUser.hasPrivilege(privilege));
    assertTrue(currentUser.toString().contains(privilege));
  }
}
