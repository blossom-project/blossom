package fr.blossom.ui.api.administration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import fr.blossom.core.association_user_group.AssociationUserGroupDTO;
import fr.blossom.core.association_user_group.AssociationUserGroupService;
import fr.blossom.core.group.GroupDTO;
import fr.blossom.core.group.GroupService;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.api.administration.MembershipsApiController.AssociationUserGroupForm;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class MembershipsApiControllerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private AssociationUserGroupService service;

  @Mock
  private UserService userService;

  @Mock
  private GroupService groupService;

  private MembershipsApiController controller;

  @Before
  public void setUp() {
    controller = new MembershipsApiController(service, userService, groupService);
  }

  @Test
  public void should_get_one_id_not_found() {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<AssociationUserGroupDTO> response = controller.get(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_get_one_id_found() {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new AssociationUserGroupDTO());
    ResponseEntity<AssociationUserGroupDTO> response = controller.get(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }


  @Test
  public void should_get_list_by_nothing() {
    thrown.expect(IllegalArgumentException.class);
    controller.list(null, null);
  }

  @Test
  public void should_get_list_by_two_much() {
    thrown.expect(IllegalArgumentException.class);
    controller.list(1L, 1L);
  }

  @Test
  public void should_get_list_by_userId_not_found() {
    Long id = 1L;
    when(userService.getOne(eq(id))).thenReturn(null);
    ResponseEntity<List<AssociationUserGroupDTO>> response = controller.list(id, null);
    verify(userService, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_get_list_by_userId_found() {
    Long id = 1L;
    when(service.getAllLeft(any(UserDTO.class)))
      .thenReturn(Lists.newArrayList(new AssociationUserGroupDTO()));
    when(userService.getOne(eq(id))).thenReturn(new UserDTO());
    ResponseEntity<List<AssociationUserGroupDTO>> response = controller.list(id, null);
    verify(userService, times(1)).getOne(eq(id));
    verify(service, times(1)).getAllLeft(any(UserDTO.class));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_get_list_by_groupId_not_found() {
    Long id = 1L;
    when(groupService.getOne(eq(id))).thenReturn(null);
    ResponseEntity<List<AssociationUserGroupDTO>> response = controller.list(null, id);
    verify(groupService, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_get_list_by_groupId_found() {
    Long id = 1L;
    when(service.getAllRight(any(GroupDTO.class)))
      .thenReturn(Lists.newArrayList(new AssociationUserGroupDTO()));
    when(groupService.getOne(eq(id))).thenReturn(new GroupDTO());
    ResponseEntity<List<AssociationUserGroupDTO>> response = controller.list(null, id);
    verify(groupService, times(1)).getOne(eq(id));
    verify(service, times(1)).getAllRight(any(GroupDTO.class));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_associate() {
    AssociationUserGroupForm form = new AssociationUserGroupForm();
    form.setUserId(1L);
    form.setGroupId(1L);

    when(userService.getOne(anyLong())).thenReturn(new UserDTO());
    when(groupService.getOne(anyLong())).thenReturn(new GroupDTO());
    when(service.associate(any(UserDTO.class), any(GroupDTO.class)))
      .thenReturn(new AssociationUserGroupDTO());

    ResponseEntity<AssociationUserGroupDTO> response = controller.associate(form);

    verify(userService, times(1)).getOne(anyLong());
    verify(groupService, times(1)).getOne(anyLong());
    verify(service, times(1)).associate(any(UserDTO.class), any(GroupDTO.class));

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_associate_user_not_found() {
    AssociationUserGroupForm form = new AssociationUserGroupForm();
    form.setUserId(1L);
    form.setGroupId(1L);

    when(userService.getOne(anyLong())).thenReturn(null);
    when(groupService.getOne(anyLong())).thenReturn(new GroupDTO());
    ResponseEntity<AssociationUserGroupDTO> response = controller.associate(form);

    verify(userService, times(1)).getOne(anyLong());
    verify(groupService, times(1)).getOne(anyLong());

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_associate_group_not_found() {
    AssociationUserGroupForm form = new AssociationUserGroupForm();
    form.setUserId(1L);
    form.setGroupId(1L);

    when(userService.getOne(any(Long.class))).thenReturn(new UserDTO());
    when(groupService.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<AssociationUserGroupDTO> response = controller.associate(form);

    verify(userService, times(1)).getOne(any(Long.class));
    verify(groupService, times(1)).getOne(any(Long.class));

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_associate_user_and_group_not_found() {
    AssociationUserGroupForm form = new AssociationUserGroupForm();
    form.setUserId(1L);
    form.setGroupId(1L);

    when(userService.getOne(anyLong())).thenReturn(null);
    when(groupService.getOne(anyLong())).thenReturn(null);
    ResponseEntity<AssociationUserGroupDTO> response = controller.associate(form);

    verify(userService, times(1)).getOne(any(Long.class));
    verify(groupService, times(1)).getOne(any(Long.class));

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }


  @Test
  public void should_dissociate_by_null_id() {
    thrown.expect(IllegalArgumentException.class);
    controller.dissociate((Long) null);
  }

  @Test
  public void should_dissociate_by_id() {
    when(service.getOne(anyLong())).thenReturn(new AssociationUserGroupDTO());
    ResponseEntity<Void> response = controller.dissociate(1L);

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NO_CONTENT);
  }

  @Test
  public void should_dissociate_by_id_not_found() {
    when(service.getOne(anyLong())).thenReturn(null);
    ResponseEntity<Void> response = controller.dissociate(1L);

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }


  @Test
  public void should_dissociate_by_userId_null_and_groupId_null() {
    ResponseEntity<Void> response = controller.dissociate(new AssociationUserGroupForm());

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_dissociate_by_userId_and_groupId() {
    when(userService.getOne(anyLong())).thenReturn(new UserDTO());
    when(groupService.getOne(anyLong())).thenReturn(new GroupDTO());

    AssociationUserGroupForm form = new AssociationUserGroupForm();
    form.setGroupId(1L);
    form.setUserId(1L);

    ResponseEntity<Void> response = controller.dissociate(form);

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NO_CONTENT);
  }
}
