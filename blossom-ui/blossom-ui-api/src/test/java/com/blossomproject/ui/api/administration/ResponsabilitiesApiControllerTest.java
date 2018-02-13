package com.blossomproject.ui.api.administration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.blossomproject.core.association_user_role.AssociationUserRoleDTO;
import com.blossomproject.core.association_user_role.AssociationUserRoleService;
import com.blossomproject.core.role.RoleDTO;
import com.blossomproject.core.role.RoleService;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.blossomproject.ui.api.administration.ResponsabilitiesApiController.AssociationUserRoleForm;
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
public class ResponsabilitiesApiControllerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private AssociationUserRoleService service;

  @Mock
  private UserService userService;

  @Mock
  private RoleService roleService;

  private ResponsabilitiesApiController controller;

  @Before
  public void setUp() {
    controller = new ResponsabilitiesApiController(service, userService, roleService);
  }

  @Test
  public void should_get_one_id_not_found() {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<AssociationUserRoleDTO> response = controller.get(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_get_one_id_found() {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new AssociationUserRoleDTO());
    ResponseEntity<AssociationUserRoleDTO> response = controller.get(id);
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
    ResponseEntity<List<AssociationUserRoleDTO>> response = controller.list(id, null);
    verify(userService, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_get_list_by_userId_found() {
    Long id = 1L;
    when(service.getAllLeft(any(UserDTO.class)))
      .thenReturn(Lists.newArrayList(new AssociationUserRoleDTO()));
    when(userService.getOne(eq(id))).thenReturn(new UserDTO());
    ResponseEntity<List<AssociationUserRoleDTO>> response = controller.list(id, null);
    verify(userService, times(1)).getOne(eq(id));
    verify(service, times(1)).getAllLeft(any(UserDTO.class));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_get_list_by_roleId_not_found() {
    Long id = 1L;
    when(roleService.getOne(eq(id))).thenReturn(null);
    ResponseEntity<List<AssociationUserRoleDTO>> response = controller.list(null, id);
    verify(roleService, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_get_list_by_roleId_found() {
    Long id = 1L;
    when(service.getAllRight(any(RoleDTO.class)))
      .thenReturn(Lists.newArrayList(new AssociationUserRoleDTO()));
    when(roleService.getOne(eq(id))).thenReturn(new RoleDTO());
    ResponseEntity<List<AssociationUserRoleDTO>> response = controller.list(null, id);
    verify(roleService, times(1)).getOne(eq(id));
    verify(service, times(1)).getAllRight(any(RoleDTO.class));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_associate() {
    AssociationUserRoleForm form = new AssociationUserRoleForm();
    form.setUserId(1L);
    form.setRoleId(1L);

    when(userService.getOne(anyLong())).thenReturn(new UserDTO());
    when(roleService.getOne(anyLong())).thenReturn(new RoleDTO());
    when(service.associate(any(UserDTO.class), any(RoleDTO.class)))
      .thenReturn(new AssociationUserRoleDTO());
    ResponseEntity<AssociationUserRoleDTO> response = controller.associate(form);

    verify(userService, times(1)).getOne(anyLong());
    verify(roleService, times(1)).getOne(anyLong());
    verify(service, times(1)).associate(any(UserDTO.class), any(RoleDTO.class));

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_associate_user_not_found() {
    AssociationUserRoleForm form = new AssociationUserRoleForm();
    form.setUserId(1L);
    form.setRoleId(1L);

    when(userService.getOne(anyLong())).thenReturn(null);
    when(roleService.getOne(anyLong())).thenReturn(new RoleDTO());
    ResponseEntity<AssociationUserRoleDTO> response = controller.associate(form);

    verify(userService, times(1)).getOne(anyLong());
    verify(roleService, times(1)).getOne(anyLong());

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_associate_role_not_found() {
    AssociationUserRoleForm form = new AssociationUserRoleForm();
    form.setUserId(1L);
    form.setRoleId(1L);

    when(userService.getOne(anyLong())).thenReturn(new UserDTO());
    when(roleService.getOne(anyLong())).thenReturn(null);
    ResponseEntity<AssociationUserRoleDTO> response = controller.associate(form);

    verify(userService, times(1)).getOne(anyLong());
    verify(roleService, times(1)).getOne(anyLong());

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_associate_user_and_role_not_found() {
    AssociationUserRoleForm form = new AssociationUserRoleForm();
    form.setUserId(1L);
    form.setRoleId(1L);

    when(userService.getOne(anyLong())).thenReturn(null);
    when(roleService.getOne(anyLong())).thenReturn(null);
    ResponseEntity<AssociationUserRoleDTO> response = controller.associate(form);

    verify(userService, times(1)).getOne(anyLong());
    verify(roleService, times(1)).getOne(anyLong());

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
    when(service.getOne(anyLong())).thenReturn(new AssociationUserRoleDTO());
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
  public void should_dissociate_by_userId_null_and_roleId_null() {
    ResponseEntity<Void> response = controller.dissociate(new AssociationUserRoleForm());

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_dissociate_by_userId_and_roleId() {
    when(userService.getOne(anyLong())).thenReturn(new UserDTO());
    when(roleService.getOne(anyLong())).thenReturn(new RoleDTO());

    AssociationUserRoleForm form = new AssociationUserRoleForm();
    form.setRoleId(1L);
    form.setUserId(1L);

    ResponseEntity<Void> response = controller.dissociate(form);

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NO_CONTENT);
  }
}
