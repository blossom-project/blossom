package com.blossomproject.core.association_user_role;


import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import com.blossomproject.core.role.Role;
import com.blossomproject.core.role.RoleDao;
import com.blossomproject.core.user.User;
import com.blossomproject.core.user.UserDao;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoTestContext.class})
@Transactional
public class AssociationUserRoleDaoImplIntegrationTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Autowired
  private UserDao userDao;

  @Autowired
  private RoleDao roleDao;

  @Autowired
  private AssociationUserRoleDao associationUserRoleDao;

  @Test
  public void should_not_find_when_no_user() {
    assertFalse(associationUserRoleDao.getUserExistsByPrivilege(Collections.emptyList()));
  }

  @Test
  public void should_not_find_when_user_with_no_role() {
    createUser(true);

    assertFalse(associationUserRoleDao.getUserExistsByPrivilege(Collections.emptyList()));
  }

  @Test
  public void should_not_find_when_user_with_role_no_privileges() {
    User user = createUser(true);
    Role role = createRole(Collections.emptyList());
    createAssociationUserRole(user, role);

    assertFalse(associationUserRoleDao.getUserExistsByPrivilege(Collections.singletonList(new SimplePrivilege("pri:vi:lege"))));
  }

  @Test
  public void should_not_find_when_user_with_role_not_activated() {
    User user = createUser(false);
    Role role = createRole(Collections.singletonList("pri:vi:lege"));
    createAssociationUserRole(user, role);

    assertFalse(associationUserRoleDao.getUserExistsByPrivilege(Collections.singletonList(new SimplePrivilege("pri:vi:lege"))));
  }

  @Test
  public void should_find_when_user_with_role_and_all_privileges() {
    User user = createUser(true);
    Role role = createRole(Arrays.asList("pri:vi:lege", "lege:pri:vi", "vi:lege:pri"));
    createAssociationUserRole(user, role);

    assertTrue(associationUserRoleDao.getUserExistsByPrivilege(
      Arrays.asList(new SimplePrivilege("pri:vi:lege"), new SimplePrivilege("lege:pri:vi"), new SimplePrivilege("vi:lege:pri"))));
  }

  private User createUser(boolean activated) {
    User user = new User();
    user.setIdentifier("b");
    user.setPasswordHash("l");
    user.setActivated(activated);
    user.setFirstname("o");
    user.setLastname("s");
    user.setEmail("s");
    user.setLocale(Locale.ENGLISH);
    user.setCreationUser("o");
    user.setModificationUser("m");
    return userDao.create(user);
  }

  private Role createRole(List<String> privileges) {
    Role role = new Role();
    role.setName("role" + (privileges.isEmpty() ? "" : privileges.get(0)));
    role.setDescription("description");
    role.setPrivileges(privileges);
    return roleDao.create(role);
  }

  private AssociationUserRole createAssociationUserRole(User user, Role role) {
    return associationUserRoleDao.associate(user, role);
  }

}
