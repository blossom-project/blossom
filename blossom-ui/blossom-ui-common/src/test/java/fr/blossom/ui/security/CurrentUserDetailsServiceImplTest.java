package fr.blossom.ui.security;

import static org.mockito.Matchers.any;

import com.google.common.collect.Lists;
import fr.blossom.core.association_user_role.AssociationUserRoleService;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.current_user.CurrentUser;
import fr.blossom.ui.current_user.UserDTOBuilder;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CurrentUserDetailsServiceImplTest {

  @Mock
  private UserService userService;
  @Mock
  private AssociationUserRoleService associationUserRoleService;

  @InjectMocks
  @Spy
  private CurrentUserDetailsServiceImpl currentUserDetailsService;

  @Test(expected = UsernameNotFoundException.class)
  public void loadUserByUsername_should_throw_exception_when_no_user_found() {

    // ARRANGE
    String identifier = "USER_18918";
    Mockito.doReturn(Optional.empty()).when(this.userService).getByIdentifier(identifier);

    // ACT
    this.currentUserDetailsService.loadUserByUsername(identifier);
  }

  @Test
  public void loadUserByUsername_should_return_user() {

    // ARRANGE
    String identifier = "USER_18918";

    UserDTO user = new UserDTOBuilder().identifier(identifier).passwordHash("PASSWORD_HASH")
      .toUserDTO();
    Mockito.doReturn(Optional.of(user)).when(this.userService).getByIdentifier(identifier);
    Mockito.doReturn(Lists.newArrayList()).when(this.associationUserRoleService).getAllLeft(any(UserDTO.class));

    // ACT
    CurrentUser result = this.currentUserDetailsService.loadUserByUsername(identifier);

    // ARRANGE
    Assert.assertEquals(user, result.getUser());

  }

}
