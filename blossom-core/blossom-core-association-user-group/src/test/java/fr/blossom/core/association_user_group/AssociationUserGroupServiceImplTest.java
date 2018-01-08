package fr.blossom.core.association_user_group;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import fr.blossom.core.group.GroupDTO;
import fr.blossom.core.group.GroupDTOMapper;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserDTOMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserGroupServiceImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_instanciation_succeed_on_null_parameter() throws Exception {
    new AssociationUserGroupServiceImpl(mock(AssociationUserGroupDao.class),
      mock(AssociationUserGroupDTOMapper.class),
      mock(UserDTOMapper.class),
      mock(GroupDTOMapper.class),
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_dao() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserGroupServiceImpl(null,
      mock(AssociationUserGroupDTOMapper.class),
      mock(UserDTOMapper.class),
      mock(GroupDTOMapper.class),
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserGroupServiceImpl(mock(AssociationUserGroupDao.class),
      null,
      mock(UserDTOMapper.class),
      mock(GroupDTOMapper.class),
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_user_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserGroupServiceImpl(mock(AssociationUserGroupDao.class),
      mock(AssociationUserGroupDTOMapper.class),
      null,
      mock(GroupDTOMapper.class),
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_group_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserGroupServiceImpl(mock(AssociationUserGroupDao.class),
      mock(AssociationUserGroupDTOMapper.class),
      mock(UserDTOMapper.class),
      null,
      mock(ApplicationEventPublisher.class));
  }

  @Test
  public void should_instanciation_fail_on_null_event_publisher() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserGroupServiceImpl(mock(AssociationUserGroupDao.class),
      mock(AssociationUserGroupDTOMapper.class),
      mock(UserDTOMapper.class),
      mock(GroupDTOMapper.class),
      null);
  }

  @Test
  public void should_support_association_class() throws Exception {
    AssociationUserGroupServiceImpl service = new AssociationUserGroupServiceImpl(mock(AssociationUserGroupDao.class),
      mock(AssociationUserGroupDTOMapper.class),
      mock(UserDTOMapper.class),
      mock(GroupDTOMapper.class),
      mock(ApplicationEventPublisher.class));

    assertTrue(service.supports(UserDTO.class));
    assertTrue(service.supports(GroupDTO.class));
  }
}
