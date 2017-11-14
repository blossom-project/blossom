package fr.blossom.core.user;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserDao userDao;

  @Mock
  private UserMailService userMailService;

  @Mock
  private Resource defaultAvatar;

  @Mock
  private UserDTOMapper userMapper;

  @InjectMocks
  @Spy
  private UserServiceImpl userService;

  @Test
  public void testGetByEmailWithResult() {

    String email = "test@blossom.com";
    User userToFind = new User();

    UserDTO userMapped = new UserDTO();

    BDDMockito.doReturn(userToFind).when(userDao).getByEmail(BDDMockito.eq(email));
    BDDMockito.doReturn(userMapped).when(userMapper).mapEntity(BDDMockito.eq(userToFind));

    Optional<UserDTO> userOptional = userService.getByEmail(email);
    Assert.assertTrue(userOptional.isPresent());

  }

  @Test
  public void testGetByEmailWithoutResult() {
    String email = "test@blossom.com";

    BDDMockito.doReturn(null).when(userDao).getByEmail(BDDMockito.eq(email));
    BDDMockito.doReturn(null).when(userMapper).mapEntity(BDDMockito.eq(null));

    Optional<UserDTO> userOptional = userService.getByEmail(email);
    Assert.assertFalse(userOptional.isPresent());
  }

}
