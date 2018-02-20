package com.blossomproject.ui.api.administration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SearchResult;
import com.blossomproject.core.group.GroupDTO;
import com.blossomproject.core.role.RoleDTO;
import com.blossomproject.core.user.UserCreateForm;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.blossomproject.core.user.UserUpdateForm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.tika.Tika;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

;

@RunWith(MockitoJUnitRunner.class)
public class UsersApiControllerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private UserService service;

  @Mock
  private SearchEngineImpl<UserDTO> searchEngine;

  @Mock
  private Tika tika;

  private UsersApiController controller;

  @Before
  public void setUp() {
    controller = new UsersApiController(service, searchEngine, tika);
  }

  @Test
  public void should_get_paged_users_without_query_parameter() {
    when(service.getAll(any(Pageable.class)))
      .thenAnswer(a -> new PageImpl<UserDTO>(Lists.newArrayList()));
    controller.list(null, PageRequest.of(0, 20));
    verify(service, times(1)).getAll(any(Pageable.class));
  }

  @Test
  public void should_get_paged_users_with_query_parameter() {
    when(searchEngine.search(any(String.class), any(Pageable.class)))
      .thenAnswer(a -> new SearchResult<>(0, new PageImpl<UserDTO>(Lists.newArrayList())));
    controller.list("test", PageRequest.of(0,10));
    verify(searchEngine, times(1)).search(eq("test"), any(Pageable.class));
  }

  @Test
  public void should_create_with_null_body() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    controller.create(null);
  }

  @Test
  public void should_create_with_body() throws Exception {
    UserCreateForm form = new UserCreateForm();
    when(service.create(any(UserCreateForm.class))).thenAnswer(a -> new UserDTO());
    ResponseEntity<UserDTO> response = controller.create(form);
    verify(service, times(1)).create(eq(form));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.CREATED);
  }

  @Test
  public void should_create_with_body_exception() throws Exception {
    thrown.expect(Exception.class);

    UserCreateForm form = new UserCreateForm();
    when(service.create(any(UserCreateForm.class))).thenThrow(new Exception());
    controller.create(form);
    verify(service, times(1)).create(eq(form));
  }

  @Test
  public void should_get_one_without_id() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    controller.get(null);
  }

  @Test
  public void should_get_one_with_id_found() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenAnswer(a -> {
      UserDTO userDTO = new UserDTO();
      userDTO.setId((Long) a.getArguments()[0]);
      return userDTO;
    });
    ResponseEntity<UserDTO> response = controller.get(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue((response.getBody().getId().equals(id)));
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_get_one_with_id_not_found() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<UserDTO> response = controller.get(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }


  @Test
  public void should_update_one_with_id_found() throws Exception {
    Long id = 1L;
    UserUpdateForm form = new UserUpdateForm();

    when(service.getOne(any(Long.class))).thenAnswer(a -> {
      UserDTO userDTO = new UserDTO();
      userDTO.setId((Long) a.getArguments()[0]);
      return userDTO;
    });
    when(service.update(any(Long.class), any(UserUpdateForm.class))).thenAnswer(a -> {
      UserDTO userDTO = new UserDTO();
      userDTO.setId((Long) a.getArguments()[0]);
      userDTO.setFirstname("test");
      return userDTO;
    });

    ResponseEntity<UserDTO> response = controller.update(id, form);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(1)).update(eq(id), eq(form));

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue((response.getBody().getId().equals(id)));
    Assert.assertTrue((response.getBody().getFirstname().equals("test")));
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_update_one_with_id_not_found() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<UserDTO> response = controller.update(id, new UserUpdateForm());
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }


  @Test
  public void should_delete_one_with_id_not_found() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.delete(id, false);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(0)).delete(any(UserDTO.class), anyBoolean());
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_delete_one_with_id_found_without_associations() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new UserDTO());
    when(service.delete(any(UserDTO.class), anyBoolean())).thenReturn(Optional.empty());

    ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.delete(id, false);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(1)).delete(any(UserDTO.class), anyBoolean());

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_delete_one_with_id_found_with_associations_no_force() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new UserDTO());
    when(service.delete(any(UserDTO.class), eq(false))).thenReturn(Optional.of(ImmutableMap.<Class<? extends AbstractDTO>, Long>builder().put(GroupDTO.class, 2L).put(RoleDTO.class, 5L).build()));

    ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.delete(id, false);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(1)).delete(any(UserDTO.class), anyBoolean());

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertFalse(response.getBody().isEmpty());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.CONFLICT);
  }

  @Test
  public void should_delete_one_with_id_found_with_associations_force() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new UserDTO());
    when(service.delete(any(UserDTO.class), eq(false))).thenReturn(Optional.of(ImmutableMap.<Class<? extends AbstractDTO>, Long>builder().put(GroupDTO.class, 2L).put(RoleDTO.class, 5L).build()));
    when(service.delete(any(UserDTO.class), eq(true))).thenReturn(Optional.empty());

    ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.delete(id, false);

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertFalse(response.getBody().isEmpty());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.CONFLICT);

    response = controller.delete(id, true);

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);


    verify(service, times(2)).getOne(eq(id));
    verify(service, times(2)).delete(any(UserDTO.class), anyBoolean());

  }

  @Test
  public void should_get_avatar_with_id_found() throws Exception {
    Long id = 1L;
    InputStream avatar = new ByteArrayInputStream("test".getBytes());
    when(service.loadAvatar(any(Long.class))).thenReturn(avatar);
    when(tika.detect(any(InputStream.class))).thenReturn("image/jpeg");
    ResponseEntity<InputStreamResource> response = controller.displayAvatar(id);
    verify(service, times(1)).loadAvatar(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue((response.getBody().getInputStream().equals(avatar)));
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

 @Test
  public void should_get_avatar_with_id_not_found() throws Exception {
    Long id = 1L;
    InputStream defaultAvatar = new ByteArrayInputStream("defaultAvatar".getBytes());
    when(service.loadAvatar(any(Long.class))).thenReturn(defaultAvatar);
    when(tika.detect(any(InputStream.class))).thenReturn("image/jpeg");
    ResponseEntity<InputStreamResource> response = controller.displayAvatar(id);
    verify(service, times(1)).loadAvatar(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue((response.getBody().getInputStream().equals(defaultAvatar)));
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_update_avatar_with_id_found() throws Exception {
    Long id = 1L;

    when(service.getOne(any(Long.class))).thenAnswer(a -> {
      UserDTO userDTO = new UserDTO();
      userDTO.setId((Long) a.getArguments()[0]);
      return userDTO;
    });

    MultipartFile multipartFile = new MockMultipartFile("testFile", "content".getBytes());

    controller.updateAvatar(id, multipartFile);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(1)).updateAvatar(eq(id), eq(multipartFile.getBytes()));
  }

  @Test
  public void should_update_avatar_with_id_not_found() throws Exception {
    Long id = 1L;

    when(service.getOne(any(Long.class))).thenReturn(null);

    MultipartFile multipartFile = new MockMultipartFile("testFile", "content".getBytes());

    thrown.expect(NoSuchElementException.class);
    thrown.expectMessage(String.format("User=%s not found", id));

    controller.updateAvatar(id, multipartFile);
  }
}
