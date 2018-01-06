package fr.blossom.core.user;

import com.google.common.io.ByteStreams;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.event.CreatedEvent;
import fr.blossom.core.common.event.UpdatedEvent;
import fr.blossom.core.common.service.AssociationServicePlugin;
import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import fr.blossom.core.common.utils.action_token.ActionToken;
import fr.blossom.core.common.utils.action_token.ActionTokenService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserDao userDao;

  @Mock
  private ActionTokenService tokenService;

  @Mock
  private UserMailService userMailService;

  @Mock
  private Resource defaultAvatar;

  @Mock
  private UserDTOMapper userMapper;

  @Mock
  private ApplicationEventPublisher publisher;
  @Mock
  private PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationRegistry;

  @InjectMocks
  @Spy
  private UserServiceImpl userService;

  @Test
  public void test_get_by_email_optional_present() {
    BDDMockito.given(userDao.getByEmail(BDDMockito.any(String.class))).willReturn(new User());
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(new UserDTO());

    Assert.assertTrue(userService.getByEmail("any").isPresent());
  }

  @Test
  public void test_get_by_email_optional_not_present() {
    BDDMockito.given(userDao.getByEmail(BDDMockito.any(String.class))).willReturn(null);
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(null);

    Assert.assertFalse(userService.getByEmail("any").isPresent());
  }

  @Test
  public void test_get_by_identifier_optional_present() {
    BDDMockito.given(userDao.getByIdentifier(BDDMockito.any(String.class))).willReturn(new User());
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(new UserDTO());

    Assert.assertTrue(userService.getByIdentifier("any").isPresent());
  }

  @Test
  public void test_get_by_identifier_optional_not_present() {
    BDDMockito.given(userDao.getByIdentifier(BDDMockito.any(String.class))).willReturn(null);
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(null);

    Assert.assertFalse(userService.getByIdentifier("any").isPresent());
  }

  @Test
  public void test_get_by_id_optional_present() {
    BDDMockito.given(userDao.getByIdentifier(BDDMockito.any(String.class))).willReturn(new User());
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(new UserDTO());
    Assert.assertTrue(userService.getByIdentifier("any").isPresent());
  }

  @Test
  public void test_get_by_id_optional_not_present() {
    BDDMockito.given(userDao.getOne(BDDMockito.anyLong())).willReturn(null);
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(null);

    Assert.assertFalse(userService.getById(123456789l).isPresent());
  }

  @Test
  public void test_create_user() throws Exception {
    UserCreateForm userCreateForm = new UserCreateForm();
    String passwordHash = "someHash";
    UserDTO userSaved = new UserDTO();
    userSaved.setPasswordHash(passwordHash);
    doReturn(passwordHash).when(userService).generateRandomPasswordHash();
    BDDMockito.given(userDao.create(BDDMockito.any(User.class))).willReturn(new User());
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(userSaved);

    UserDTO result = userService.create(userCreateForm);
    Assert.assertEquals(userSaved, result);
    Assert.assertEquals(passwordHash, result.getPasswordHash());
    BDDMockito.verify(userMailService, BDDMockito.times(1))
      .sendAccountCreationEmail(BDDMockito.any(UserDTO.class), BDDMockito.anyString());
    BDDMockito.verify(publisher, BDDMockito.times(1)).publishEvent(BDDMockito.any(CreatedEvent.class));

  }

  @Test
  public void test_update_user_from_form() {
    UserDTO userToUpdate = new UserDTO();
    userToUpdate.setId(123456789l);
    doReturn(userToUpdate).when(userService).getOne(BDDMockito.anyLong());
    doReturn(userToUpdate).when(userService).update(BDDMockito.anyLong(), BDDMockito.any(UserDTO.class));

    Assert.assertEquals(userToUpdate, userService.update(123456789L, new UserUpdateForm(userToUpdate)));
    BDDMockito.verify(userService, BDDMockito.times(1)).getOne(BDDMockito.anyLong());
    BDDMockito.verify(userService, BDDMockito.times(1)).update(BDDMockito.anyLong(), BDDMockito.any(UserDTO.class));
  }

  @Test
  public void test_update_activation() throws Exception {
    UserDTO userToActivate = new UserDTO();
    BDDMockito.given(userDao.updateActivation(BDDMockito.anyLong(), BDDMockito.any(Boolean.class))).willReturn(
        new User());
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(userToActivate);

    Assert.assertEquals(userToActivate, userService.updateActivation(123456789l, true));
    BDDMockito.verify(publisher, BDDMockito.times(1)).publishEvent(BDDMockito.any(UpdatedEvent.class));
  }

  @Test
  public void test_update_password() throws Exception {
    UserDTO userToUpdate = new UserDTO();
    BDDMockito.given(passwordEncoder.encode(BDDMockito.anyString())).willReturn("any");
    BDDMockito.given(userDao.updatePassword(BDDMockito.anyLong(), BDDMockito.anyString())).willReturn(new User());
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(userToUpdate);

    Assert.assertEquals(userToUpdate, userService.updatePassword(123456789l, "any"));
    BDDMockito.verify(publisher, BDDMockito.times(1)).publishEvent(BDDMockito.any(UpdatedEvent.class));
  }

  @Test
  public void test_update_last_connection() throws Exception {
    UserDTO userToUpdate = new UserDTO();
    BDDMockito.given(userDao.updateLastConnection(BDDMockito.anyLong(), BDDMockito.any(Date.class))).willReturn(
        new User());
    BDDMockito.given(userMapper.mapEntity(BDDMockito.any(User.class))).willReturn(userToUpdate);

    Assert.assertEquals(userToUpdate, userService.updateLastConnection(123456789l, new Date()));
    BDDMockito.verify(publisher, BDDMockito.times(1)).publishEvent(BDDMockito.any(UpdatedEvent.class));
  }

  @Test
  public void test_ask_password_change() throws Exception {
    UserDTO userToUpdate = new UserDTO();
    doReturn(userToUpdate).when(userService).updatePassword(BDDMockito.anyLong(), BDDMockito.anyString());

    userService.askPasswordChange(123456789L);
    BDDMockito.verify(userMailService, BDDMockito.times(1))
      .sendChangePasswordEmail(BDDMockito.any(UserDTO.class), BDDMockito.anyString());
  }

  @Test
  public void test_update_avatar_user_not_null() throws Exception {
    doReturn(new UserDTO()).when(userService).getOne(BDDMockito.anyLong());

    userService.updateAvatar(123456789L, new byte[]{});
    BDDMockito.verify(userDao, BDDMockito.times(1)).updateAvatar(BDDMockito.anyLong(), BDDMockito.any(byte[].class));
    BDDMockito.verify(publisher, BDDMockito.times(1)).publishEvent(BDDMockito.any(UpdatedEvent.class));

  }

  @Test
  public void test_update_avatar_user_null() throws Exception {
    doReturn(null).when(userService).getOne(BDDMockito.anyLong());

    userService.updateAvatar(123456789L, new byte[]{});
    BDDMockito.verify(userDao, BDDMockito.times(0)).updateAvatar(BDDMockito.anyLong(), BDDMockito.any(byte[].class));
    BDDMockito.verify(publisher, BDDMockito.times(1)).publishEvent(BDDMockito.any(UpdatedEvent.class));
  }

  @Test
  public void test_load_avatar_not_null() throws Exception {
    User userToReturn = new User();
    byte[] avatar = new byte[]{0, 1,2,3};
    userToReturn.setAvatar(avatar);
    BDDMockito.given(userDao.getOne(BDDMockito.anyLong())).willReturn(userToReturn);

    Assert.assertArrayEquals(avatar, ByteStreams.toByteArray(userService.loadAvatar(1L)));
  }

  @Test
  public void test_load_avatar_user_null_so_fallback_to_default() throws Exception {
    byte[] bytes = new byte[]{};
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    BDDMockito.given(userDao.getOne(BDDMockito.anyLong())).willReturn(null);
    BDDMockito.given(defaultAvatar.getInputStream()).willReturn(inputStream);

    userService.loadAvatar(123456789L);
    BDDMockito.verify(defaultAvatar, BDDMockito.times(1)).getInputStream();
  }

  @Test
  public void test_load_avatar_user_not_nul_but_avatar_null_so_fallback_to_default() throws Exception {
    byte[] bytes = new byte[]{};
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    BDDMockito.given(userDao.getOne(BDDMockito.anyLong())).willReturn(new User());
    BDDMockito.given(defaultAvatar.getInputStream()).willReturn(inputStream);

    userService.loadAvatar(123456789L);
    BDDMockito.verify(defaultAvatar, BDDMockito.times(1)).getInputStream();
  }

  @Test
  public void test_generate_random_password_hash() throws Exception {
    String passwordHash = "someHash";
    BDDMockito.given(passwordEncoder.encode(BDDMockito.anyString())).willReturn(passwordHash);

    Assert.assertEquals(passwordHash, userService.generateRandomPasswordHash());
  }

  private ActionToken setup_token_service_interceptor(String ret) {
    final ActionToken actionToken = new ActionToken();
    BDDMockito.given(tokenService.generateToken(BDDMockito.any(ActionToken.class))).willAnswer(invocation -> {
      ActionToken generatedToken = (ActionToken) invocation.getArguments()[0];
      actionToken.setUserId(generatedToken.getUserId());
      actionToken.setAction(generatedToken.getAction());
      actionToken.setExpirationDate(generatedToken.getExpirationDate());
      actionToken.setAdditionalParameters(generatedToken.getAdditionalParameters());
      return ret;
    });

    return actionToken;
  }

  @Test
  public void test_generate_activation_token() throws Exception {
    String token = "token !";
    ActionToken actionToken = setup_token_service_interceptor(token);
    UserDTO userDTO = new UserDTO();
    userDTO.setId(456L);
    Assert.assertEquals(token, userService.generateActivationToken(userDTO));
    Assert.assertEquals(userDTO.getId(), actionToken.getUserId());
    Assert.assertEquals(UserService.USER_ACTIVATION, actionToken.getAction());
  }

  @Test
  public void test_generate_password_reset_token() throws Exception {
    String token = "token !";
    ActionToken actionToken = setup_token_service_interceptor(token);
    UserDTO userDTO = new UserDTO();
    userDTO.setId(123L);
    Assert.assertEquals(token, userService.generatePasswordResetToken(userDTO));
    Assert.assertEquals(userDTO.getId(), actionToken.getUserId());
    Assert.assertEquals(UserService.USER_RESET_PASSWORD, actionToken.getAction());
  }

  @Test
  public void test_get_by_activation_action_token() throws Exception {
    ActionToken actionToken = setup_token_service_interceptor("");
    UserDTO user = new UserDTO();
    user.setLastConnection(new Date());
    userService.generateActivationToken(user);
    doReturn(Optional.of(user)).when(userService).getById(BDDMockito.anyLong());

    Optional<UserDTO> userFromToken = userService.getByActionToken(actionToken);
    Assert.assertTrue(userFromToken.isPresent());
    Assert.assertEquals(user.getLastConnection(), userFromToken.get().getLastConnection());
  }

  @Test
  public void test_get_by_activation_action_token_invalid_user() throws Exception {
    ActionToken actionToken = setup_token_service_interceptor("");
    UserDTO user = new UserDTO();
    user.setLastConnection(new Date());
    userService.generateActivationToken(user);
    doReturn(Optional.empty()).when(userService).getById(BDDMockito.anyLong());

    Optional<UserDTO> userFromToken = userService.getByActionToken(actionToken);
    Assert.assertFalse(userFromToken.isPresent());
  }

  @Test
  public void test_get_by_activation_action_token_expired_by_connection() throws Exception {
    ActionToken actionToken = setup_token_service_interceptor("");
    UserDTO user = new UserDTO();
    user.setLastConnection(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)));
    userService.generateActivationToken(user);
    doReturn(Optional.of(user)).when(userService).getById(BDDMockito.anyLong());

    Optional<UserDTO> userFromToken = userService.getByActionToken(actionToken);
    Assert.assertFalse(userFromToken.isPresent());
  }

  @Test
  public void test_user_service_impl_nothing_null() throws Exception {
    new UserServiceImpl(userDao, userMapper, publisher, associationRegistry, passwordEncoder, tokenService, userMailService, defaultAvatar);
  }

  @Test
  public void test_user_service_impl_password_encoder_null() throws Exception {
    thrown.expect(NullPointerException.class);
    new UserServiceImpl(userDao, userMapper, publisher, associationRegistry, null, tokenService, userMailService, defaultAvatar);
  }

  @Test
  public void test_user_service_impl_token_service_null() throws Exception {
    thrown.expect(NullPointerException.class);
    new UserServiceImpl(userDao, userMapper, publisher, associationRegistry, passwordEncoder, null, userMailService, defaultAvatar);
  }

  @Test
  public void test_user_service_impl_dao_null() throws Exception {
    thrown.expect(NullPointerException.class);
    new UserServiceImpl(null, userMapper, publisher, associationRegistry, passwordEncoder, tokenService, userMailService, defaultAvatar);
  }

  @Test
  public void test_user_service_mail_service_null() throws Exception {
    thrown.expect(NullPointerException.class);
    new UserServiceImpl(userDao, userMapper, publisher, associationRegistry, passwordEncoder, tokenService,null, defaultAvatar);
  }

  @Test
  public void test_user_service_default_avatar_null() throws Exception {
    thrown.expect(NullPointerException.class);
    new UserServiceImpl(userDao, userMapper, publisher, associationRegistry, passwordEncoder, tokenService, userMailService, null);
  }
}
