package com.blossom_project.ui.api.administration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.search.SearchEngineImpl;
import com.blossom_project.core.common.search.SearchResult;
import com.blossom_project.core.group.GroupCreateForm;
import com.blossom_project.core.group.GroupDTO;
import com.blossom_project.core.group.GroupService;
import com.blossom_project.core.group.GroupUpdateForm;
import com.blossom_project.core.role.RoleDTO;
import com.blossom_project.core.user.UserDTO;
import java.util.Map;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class GroupsApiControllerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private GroupService service;

  @Mock
  private SearchEngineImpl<GroupDTO> searchEngine;

  private GroupsApiController controller;

  @Before
  public void setUp() {
    controller = new GroupsApiController(service, searchEngine);
  }

  @Test
  public void should_get_paged_groups_without_query_parameter() {
    when(service.getAll(any(Pageable.class)))
      .thenAnswer(a -> new PageImpl<GroupDTO>(Lists.newArrayList()));
    controller.list(null, PageRequest.of(0, 20));
    verify(service, times(1)).getAll(any(Pageable.class));
  }

  @Test
  public void should_get_paged_groups_with_query_parameter() {
    when(searchEngine.search(any(String.class), any(Pageable.class)))
      .thenAnswer(a -> new SearchResult<>(0, new PageImpl<GroupDTO>(Lists.newArrayList())));
    controller.list("test", PageRequest.of(0,5));
    verify(searchEngine, times(1)).search(eq("test"), any(Pageable.class));
  }

  @Test
  public void should_create_with_null_body() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    controller.create(null);
  }

  @Test
  public void should_create_with_body() throws Exception {
    GroupCreateForm form = new GroupCreateForm();
    when(service.create(any(GroupCreateForm.class))).thenAnswer(a -> new GroupDTO());
    ResponseEntity<GroupDTO> response = controller.create(form);
    verify(service, times(1)).create(eq(form));
    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.CREATED);
  }

  @Test
  public void should_create_with_body_exception() throws Exception {
    thrown.expect(Exception.class);

    GroupCreateForm form = new GroupCreateForm();
    when(service.create(any(GroupCreateForm.class))).thenThrow(new Exception());
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
      GroupDTO groupDTO = new GroupDTO();
      groupDTO.setId((Long) a.getArguments()[0]);
      return groupDTO;
    });
    ResponseEntity<GroupDTO> response = controller.get(id);
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
    ResponseEntity<GroupDTO> response = controller.get(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }


  @Test
  public void should_update_one_with_id_found() throws Exception {
    Long id = 1L;
    GroupUpdateForm form = new GroupUpdateForm();

    when(service.getOne(any(Long.class))).thenAnswer(a -> {
      GroupDTO groupDTO = new GroupDTO();
      groupDTO.setId((Long) a.getArguments()[0]);
      return groupDTO;
    });
    when(service.update(any(Long.class), any(GroupUpdateForm.class))).thenAnswer(a -> {
      GroupDTO groupDTO = new GroupDTO();
      groupDTO.setId((Long) a.getArguments()[0]);
      groupDTO.setName("test");
      return groupDTO;
    });

    ResponseEntity<GroupDTO> response = controller.update(id, form);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(1)).update(eq(id), eq(form));

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertTrue((response.getBody().getId().equals(id)));
    Assert.assertTrue((response.getBody().getName().equals("test")));
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_update_one_with_id_not_found() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(null);
    ResponseEntity<GroupDTO> response = controller.update(id, new GroupUpdateForm());
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
    verify(service, times(0)).delete(any(GroupDTO.class), anyBoolean());
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_delete_one_with_id_found_without_associations() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new GroupDTO());
    when(service.delete(any(GroupDTO.class), anyBoolean())).thenReturn(Optional.empty());

    ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.delete(id, false);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(1)).delete(any(GroupDTO.class), anyBoolean());

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_delete_one_with_id_found_with_associations_no_force() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new GroupDTO());
    when(service.delete(any(GroupDTO.class), eq(false))).thenReturn(Optional.of(
      ImmutableMap.<Class<? extends AbstractDTO>, Long>builder().put(RoleDTO.class, 2L)
        .put(UserDTO.class, 5L).build()));

    ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.delete(id, false);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(1)).delete(any(GroupDTO.class), anyBoolean());

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertFalse(response.getBody().isEmpty());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.CONFLICT);
  }

  @Test
  public void should_delete_one_with_id_found_with_associations_force() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new GroupDTO());
    when(service.delete(any(GroupDTO.class), eq(false))).thenReturn(Optional.of(
      ImmutableMap.<Class<? extends AbstractDTO>, Long>builder().put(RoleDTO.class, 2L)
        .put(UserDTO.class, 5L).build()));
    when(service.delete(any(GroupDTO.class), eq(true))).thenReturn(Optional.empty());

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
    verify(service, times(2)).delete(any(GroupDTO.class), anyBoolean());

  }
}
