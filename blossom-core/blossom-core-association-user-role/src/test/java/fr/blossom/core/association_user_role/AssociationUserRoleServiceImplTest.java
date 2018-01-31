package fr.blossom.core.association_user_role;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleDTOMapper;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserDTOMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserRoleServiceImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_instanciation_succeed_on_null_parameter() throws Exception {
    new AssociationUserRoleServiceImpl(mock(AssociationUserRoleDao.class),
      mock(AssociationUserRoleDTOMapper.class),
      mock(UserDTOMapper.class),
      mock(RoleDTOMapper.class),
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_dao() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserRoleServiceImpl(null,
      mock(AssociationUserRoleDTOMapper.class),
      mock(UserDTOMapper.class),
      mock(RoleDTOMapper.class),
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserRoleServiceImpl(mock(AssociationUserRoleDao.class),
      null,
      mock(UserDTOMapper.class),
      mock(RoleDTOMapper.class),
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_user_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserRoleServiceImpl(mock(AssociationUserRoleDao.class),
      mock(AssociationUserRoleDTOMapper.class),
      null,
      mock(RoleDTOMapper.class),
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_role_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserRoleServiceImpl(mock(AssociationUserRoleDao.class),
      mock(AssociationUserRoleDTOMapper.class),
      mock(UserDTOMapper.class),
      null,
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_event_publisher() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserRoleServiceImpl(mock(AssociationUserRoleDao.class),
      mock(AssociationUserRoleDTOMapper.class),
      mock(UserDTOMapper.class),
      mock(RoleDTOMapper.class),
      null);
  }

  @Test
  public void should_support_association_class() throws Exception {
    AssociationUserRoleServiceImpl service = new AssociationUserRoleServiceImpl(
      mock(AssociationUserRoleDao.class),
      mock(AssociationUserRoleDTOMapper.class),
      mock(UserDTOMapper.class),
      mock(RoleDTOMapper.class),
      mock(ApplicationEventPublisher.class));

    assertTrue(service.supports(UserDTO.class));
    assertTrue(service.supports(RoleDTO.class));
  }
}
