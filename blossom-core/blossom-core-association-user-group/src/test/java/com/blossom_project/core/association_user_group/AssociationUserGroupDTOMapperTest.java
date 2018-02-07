package com.blossom_project.core.association_user_group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blossom_project.core.group.Group;
import com.blossom_project.core.group.GroupDTO;
import com.blossom_project.core.group.GroupDTOMapper;
import com.blossom_project.core.user.User;
import com.blossom_project.core.user.UserDTO;
import com.blossom_project.core.user.UserDTOMapper;
import java.util.Date;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserGroupDTOMapperTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private UserDTOMapper userMapper;
  private GroupDTOMapper groupMapper;
  private AssociationUserGroupDTOMapper mapper;

  @Before
  public void setUp() throws Exception {
    this.userMapper = mock(UserDTOMapper.class);
    this.groupMapper = mock(GroupDTOMapper.class);
    this.mapper = spy(new AssociationUserGroupDTOMapper(userMapper, groupMapper));
  }

  @Test
  public void should_instanciate_succeed() throws Exception {
    new AssociationUserGroupDTOMapper(mock(UserDTOMapper.class), mock(GroupDTOMapper.class));
  }

  @Test
  public void should_instanciate_fail_with_null_user_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserGroupDTOMapper(null, mock(GroupDTOMapper.class));
  }

  @Test
  public void should_instanciate_fail_with_null_group_mapper() throws Exception {
    thrown.expect(NullPointerException.class);
    new AssociationUserGroupDTOMapper(mock(UserDTOMapper.class), null);
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
    Group group = new Group();

    AssociationUserGroup entity = new AssociationUserGroup();
    entity.setA(user);
    entity.setB(group);

    when(this.userMapper.mapEntity(any(User.class))).thenReturn(new UserDTO());
    when(this.groupMapper.mapEntity(any(Group.class))).thenReturn(new GroupDTO());

    AssociationUserGroupDTO result = this.mapper.mapEntity(entity);

    assertNotNull(result);
    assertNotNull(result.getA());
    assertNotNull(result.getB());
    verify(this.userMapper, times(1)).mapEntity(eq(user));
    verify(this.groupMapper, times(1)).mapEntity(eq(group));
  }

  @Test
  public void should_map_dto() {
    UserDTO user = new UserDTO();
    GroupDTO group = new GroupDTO();

    AssociationUserGroupDTO dto= new AssociationUserGroupDTO();
    dto.setA(user);
    dto.setB(group);

    when(this.userMapper.mapDto(any(UserDTO.class))).thenReturn(new User());
    when(this.groupMapper.mapDto(any(GroupDTO.class))).thenReturn(new Group());

    AssociationUserGroup result = this.mapper.mapDto(dto);

    assertNotNull(result);
    assertNotNull(result.getA());
    assertNotNull(result.getB());
    verify(this.userMapper, times(1)).mapDto(eq(user));
    verify(this.groupMapper, times(1)).mapDto(eq(group));
  }

  @Test
  public void should_map_common_fields() {
    AssociationUserGroup entity = new AssociationUserGroup();
    entity.setId(1L);
    entity.setCreationDate(new Date());
    entity.setModificationDate(new Date());
    entity.setCreationUser("test");
    entity.setModificationUser("testUpdate");

    AssociationUserGroupDTO result = this.mapper.mapEntity(entity);
    assertEquals(result.getId(), entity.getId());
    assertEquals(result.getCreationDate(),entity.getCreationDate());
    assertEquals(result.getModificationDate(),entity.getModificationDate());
    assertEquals(result.getCreationUser(),entity.getCreationUser());
    assertEquals(result.getModificationUser(),entity.getModificationUser());


  }
}
