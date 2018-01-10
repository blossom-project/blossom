package fr.blossom.core.user;

import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;;

import fr.blossom.core.user.User.Civility;

@RunWith(MockitoJUnitRunner.class)
public class UserDTOMapperTest {

  @Spy
  private UserDTOMapper userMapper;

  @Test
  public void test_map_entity_not_null() throws Exception {
    Date now = new Date();
    User userToMap = new User();
    userToMap.setId(123456789L);
    userToMap.setCreationDate(now);
    userToMap.setModificationDate(now);
    userToMap.setCreationUser("blossom");
    userToMap.setModificationUser("blossom");
    userToMap.setIdentifier("someIdentifier");
    userToMap.setPasswordHash("someHash");
    userToMap.setDescription("someDescription");
    userToMap.setActivated(true);
    userToMap.setLastConnection(now);
    userToMap.setCivility(Civility.MAN);
    userToMap.setFirstname("someFirstname");
    userToMap.setLastname("someLastname");
    userToMap.setEmail("someEmail");
    userToMap.setPhone("somePhone");
    userToMap.setCompany("someCompany");
    userToMap.setFunction("someFunction");
    userToMap.setAvatar(new byte[]{0});
    userToMap.setLocale(Locale.FRANCE);

    UserDTO mappedUser = userMapper.mapEntity(userToMap);

    Assert.assertEquals(userToMap.getId(), mappedUser.getId());
    Assert.assertEquals(userToMap.getCreationDate(), mappedUser.getCreationDate());
    Assert.assertEquals(userToMap.getCreationUser(), mappedUser.getCreationUser());
    Assert.assertEquals(userToMap.getModificationDate(), mappedUser.getModificationDate());
    Assert.assertEquals(userToMap.getModificationUser(), mappedUser.getModificationUser());
    Assert.assertEquals(userToMap.getIdentifier(), mappedUser.getIdentifier());
    Assert.assertEquals(userToMap.getPasswordHash(), mappedUser.getPasswordHash());
    Assert.assertEquals(userToMap.getDescription(), mappedUser.getDescription());
    Assert.assertEquals(userToMap.isActivated(), mappedUser.isActivated());
    Assert.assertEquals(userToMap.getLastConnection(), mappedUser.getLastConnection());
    Assert.assertEquals(userToMap.getCivility(), mappedUser.getCivility());
    Assert.assertEquals(userToMap.getFirstname(), mappedUser.getFirstname());
    Assert.assertEquals(userToMap.getLastname(), mappedUser.getLastname());
    Assert.assertEquals(userToMap.getEmail(), mappedUser.getEmail());
    Assert.assertEquals(userToMap.getPhone(), mappedUser.getPhone());
    Assert.assertEquals(userToMap.getCompany(), mappedUser.getCompany());
    Assert.assertEquals(userToMap.getFunction(), mappedUser.getFunction());
    Assert.assertEquals(userToMap.getLocale(), mappedUser.getLocale());

  }

  @Test
  public void test_map_entity_null() throws Exception {
    Assert.assertNull(userMapper.mapEntity(null));
  }

  @Test
  public void test_map_dto_not_null() throws Exception {
    Date now = new Date();
    UserDTO userToMap = new UserDTO();
    userToMap.setId(123456789L);
    userToMap.setCreationDate(now);
    userToMap.setModificationDate(now);
    userToMap.setCreationUser("blossom");
    userToMap.setModificationUser("blossom");
    userToMap.setIdentifier("someIdentifier");
    userToMap.setPasswordHash("someHash");
    userToMap.setDescription("someDescription");
    userToMap.setActivated(true);
    userToMap.setLastConnection(now);
    userToMap.setCivility(Civility.MAN);
    userToMap.setFirstname("someFirstname");
    userToMap.setLastname("someLastname");
    userToMap.setEmail("someEmail");
    userToMap.setPhone("somePhone");
    userToMap.setCompany("someCompany");
    userToMap.setFunction("someFunction");
    userToMap.setLocale(Locale.FRANCE);

    User mappedUser = userMapper.mapDto(userToMap);

    Assert.assertEquals(userToMap.getId(), mappedUser.getId());
    Assert.assertEquals(userToMap.getCreationDate(), mappedUser.getCreationDate());
    Assert.assertEquals(userToMap.getCreationUser(), mappedUser.getCreationUser());
    Assert.assertEquals(userToMap.getModificationDate(), mappedUser.getModificationDate());
    Assert.assertEquals(userToMap.getModificationUser(), mappedUser.getModificationUser());
    Assert.assertEquals(userToMap.getIdentifier(), mappedUser.getIdentifier());
    Assert.assertEquals(userToMap.getPasswordHash(), mappedUser.getPasswordHash());
    Assert.assertEquals(userToMap.getDescription(), mappedUser.getDescription());
    Assert.assertEquals(userToMap.isActivated(), mappedUser.isActivated());
    Assert.assertEquals(userToMap.getLastConnection(), mappedUser.getLastConnection());
    Assert.assertEquals(userToMap.getCivility(), mappedUser.getCivility());
    Assert.assertEquals(userToMap.getFirstname(), mappedUser.getFirstname());
    Assert.assertEquals(userToMap.getLastname(), mappedUser.getLastname());
    Assert.assertEquals(userToMap.getEmail(), mappedUser.getEmail());
    Assert.assertEquals(userToMap.getPhone(), mappedUser.getPhone());
    Assert.assertEquals(userToMap.getCompany(), mappedUser.getCompany());
    Assert.assertEquals(userToMap.getFunction(), mappedUser.getFunction());
    Assert.assertEquals(userToMap.getLocale(), mappedUser.getLocale());
  }

  @Test
  public void test_map_dto_null() throws Exception {
    Assert.assertNull(userMapper.mapDto(null));
  }

}
