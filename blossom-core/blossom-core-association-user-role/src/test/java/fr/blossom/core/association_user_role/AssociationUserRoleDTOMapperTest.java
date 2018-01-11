package fr.blossom.core.association_user_role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.blossom.core.role.Role;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleDTOMapper;
import fr.blossom.core.user.User;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserDTOMapper;
import java.util.Date;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserRoleDTOMapperTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private UserDTOMapper userMapper;
  private RoleDTOMapper roleMapper;
  private AssociationUserRoleDTOMapper mapper;

  @Before
  public void setUp() throws Exception {
    this.userMapper = mock(UserDTOMapper.class);
    this.roleMapper = mock(RoleDTOMapper.class);
    this.mapper = spy(new AssociationUserRoleDTOMapper(userMapper, roleMapper));
  }

  @Test
  public void should_instanciate_succeed() throws Exception {
    new AssociationUserRoleDTOMapper(mock(UserDTOMapper.class), mock(RoleDTOMapper.class));
  }

  @Test
  public void should_instanciate_fail_with_null_user_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserRoleDTOMapper(null, mock(RoleDTOMapper.class));
  }

  @Test
  public void should_instanciate_fail_with_null_role_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserRoleDTOMapper(mock(UserDTOMapper.class), null);
  }

  @Test
  public void should_map_null_entity() {
    assertNull(this.mapper.mapEntity(null));
  }

  @Test
  public void should_map_null_dto() {
    assertNull(this.mapper.mapDto(null));
  }

  @Test
  public void should_map_entity() {
    User user = new User();
    Role role = new Role();

    AssociationUserRole entity = new AssociationUserRole();
    entity.setA(user);
    entity.setB(role);

    when(this.userMapper.mapEntity(any(User.class))).thenReturn(new UserDTO());
    when(this.roleMapper.mapEntity(any(Role.class))).thenReturn(new RoleDTO());

    AssociationUserRoleDTO result = this.mapper.mapEntity(entity);

    assertNotNull(result);
    assertNotNull(result.getA());
    assertNotNull(result.getB());
    verify(this.userMapper, times(1)).mapEntity(eq(user));
    verify(this.roleMapper, times(1)).mapEntity(eq(role));
  }

  @Test
  public void should_map_dto() {
    UserDTO user = new UserDTO();
    RoleDTO role = new RoleDTO();

    AssociationUserRoleDTO dto= new AssociationUserRoleDTO();
    dto.setA(user);
    dto.setB(role);

    when(this.userMapper.mapDto(any(UserDTO.class))).thenReturn(new User());
    when(this.roleMapper.mapDto(any(RoleDTO.class))).thenReturn(new Role());

    AssociationUserRole result = this.mapper.mapDto(dto);

    assertNotNull(result);
    assertNotNull(result.getA());
    assertNotNull(result.getB());
    verify(this.userMapper, times(1)).mapDto(eq(user));
    verify(this.roleMapper, times(1)).mapDto(eq(role));
  }

  @Test
  public void should_map_common_fields() {
    AssociationUserRole entity = new AssociationUserRole();
    entity.setId(1L);
    entity.setCreationDate(new Date());
    entity.setModificationDate(new Date());
    entity.setCreationUser("test");
    entity.setModificationUser("testUpdate");

    AssociationUserRoleDTO result = this.mapper.mapEntity(entity);
    assertEquals(result.getId(), entity.getId());
    assertEquals(result.getCreationDate(),entity.getCreationDate());
    assertEquals(result.getModificationDate(),entity.getModificationDate());
    assertEquals(result.getCreationUser(),entity.getCreationUser());
    assertEquals(result.getModificationUser(),entity.getModificationUser());


  }
}
