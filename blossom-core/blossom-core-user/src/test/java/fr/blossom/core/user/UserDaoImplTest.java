package fr.blossom.core.user;

import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zoula_000 on 17/05/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestContext.class})
@Transactional
public class UserDaoImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private UserRepository userRepository;

  @Autowired
  private UserDao userDao;

  @Spy
  @InjectMocks
  private UserDaoImpl userDaoImpl;

  @Test
  public void test_get_by_email_with_result() {

    User user = new User();
    user.setFirstname("F");
    user.setLastname("L");
    user.setPasswordHash("H");
    user.setIdentifier("I");
    user.setDescription("D");
    user.setEmail("E");
    user.setLocale(Locale.FRANCE);
    userDao.create(user);

    User result = userDao.getByEmail("E");
    Assert.assertNotNull(result);
  }

  @Test
  public void test_get_by_email_without_result() {

    User result = userDao.getByEmail("E");
    Assert.assertNull(result);
  }

  @Test
  public void test_get_by_identifier_with_result() {

    User user = new User();
    user.setFirstname("F");
    user.setLastname("L");
    user.setPasswordHash("H");
    user.setIdentifier("I");
    user.setDescription("D");
    user.setEmail("E");
    user.setLocale(Locale.FRANCE);
    userDao.create(user);

    User result = userDao.getByIdentifier("I");
    Assert.assertNotNull(result);
  }

  @Test
  public void test_get_by_identifier_without_result() {

    User result = userDao.getByIdentifier("I");
    Assert.assertNull(result);
  }

  @Test
  public void test_user_dao_impl_repository_not_null() throws Exception {
    new UserDaoImpl(userRepository);
  }

  @Test
  public void test_user_dao_impl_repository_null() throws Exception {
    thrown.expect(NullPointerException.class);
    new UserDaoImpl(null);
  }

  @Test
  public void test_update_entity() throws Exception {
    User user = new User();
    user.setFirstname("F");
    user.setLastname("L");
    user.setPasswordHash("H");
    user.setIdentifier("I");
    user.setDescription("D");
    user.setEmail("E");
    user.setLocale(Locale.FRANCE);
    userDao.create(user);

    User modifiedUser = new User();
    BeanUtils.copyProperties(user, modifiedUser);
    modifiedUser.setFirstname("B");
    Assert.assertEquals("F", user.getFirstname());
    Assert.assertEquals(user, userDaoImpl.updateEntity(user, modifiedUser));
    Assert.assertEquals("B", user.getFirstname());
  }

  @Test
  public void test_update_activation() throws Exception {
    User userToFind = new User();
    userToFind.setActivated(false);
    BDDMockito.given(userRepository.findById(BDDMockito.anyLong())).willReturn(Optional.of(userToFind));
    BDDMockito.given(userRepository.save(BDDMockito.any(User.class))).willReturn(userToFind);

    Assert.assertTrue(userDaoImpl.updateActivation(123456789L, true).isActivated());
  }

  @Test
  public void test_update_password() throws Exception {
    User userToFind = new User();
    userToFind.setPasswordHash("someHash");
    BDDMockito.given(userRepository.findById(BDDMockito.anyLong())).willReturn(Optional.of(userToFind));
    BDDMockito.given(userRepository.save(BDDMockito.any(User.class))).willReturn(userToFind);

    Assert.assertEquals("any", userDaoImpl.updatePassword(123456789L, "any").getPasswordHash());
  }

  @Test
  public void test_update_avatar() throws Exception {
    User userToFind = new User();
    userToFind.setAvatar(new byte[]{0});
    BDDMockito.given(userRepository.findById(BDDMockito.anyLong())).willReturn(Optional.of(userToFind));
    BDDMockito.given(userRepository.save(BDDMockito.any(User.class))).willReturn(userToFind);

    byte[] avatar = new byte[]{1};
    Assert.assertEquals(avatar, userDaoImpl.updateAvatar(123456789L, avatar).getAvatar());
  }

  @Test
  public void test_update_last_connection() throws Exception {
    User userToFind = new User();
    BDDMockito.given(userRepository.findById(BDDMockito.anyLong())).willReturn(Optional.of(userToFind));
    BDDMockito.given(userRepository.save(BDDMockito.any(User.class))).willReturn(userToFind);
    Date now = new Date();

    Assert.assertEquals(now, userDaoImpl.updateLastConnection(123456789L, now).getLastConnection());
  }

  @Test
  public void test_update_last_connection_null_user() throws Exception {
    BDDMockito.given(userRepository.findById(BDDMockito.anyLong())).willReturn(Optional.empty());

    Assert.assertNull(userDaoImpl.updateLastConnection(123456789L, new Date()));
  }
}
