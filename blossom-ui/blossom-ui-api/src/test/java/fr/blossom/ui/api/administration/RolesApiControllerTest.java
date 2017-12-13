package fr.blossom.ui.api.administration;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.common.search.SearchResult;
import fr.blossom.core.group.GroupDTO;
import fr.blossom.core.role.RoleCreateForm;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleService;
import fr.blossom.core.role.RoleUpdateForm;
import fr.blossom.core.user.UserDTO;
import java.util.Map;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class RolesApiControllerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private RoleService service;

  @Mock
  private SearchEngineImpl<RoleDTO> searchEngine;

  private RolesApiController controller;

  @Before
  public void setUp() {
    controller = new RolesApiController(service, searchEngine);
  }

  @Test
  public void should_get_paged_roles_without_query_parameter() {
    when(service.getAll(any(Pageable.class)))
      .thenAnswer(a -> new PageImpl<RoleDTO>(Lists.newArrayList()));
    controller.list(null, new PageRequest(0, 20));
    verify(service, times(1)).getAll(any(Pageable.class));
  }

  @Test
  public void should_get_paged_roles_with_query_parameter() {
    when(searchEngine.search(any(String.class), any(Pageable.class)))
      .thenAnswer(a -> new SearchResult<>(0, new PageImpl<RoleDTO>(Lists.newArrayList())));
    controller.list("test", null);
    verify(searchEngine, times(1)).search(eq("test"), any(Pageable.class));
  }

  @Test
  public void should_create_with_null_body() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    when(service.create(any(RoleCreateForm.class))).thenAnswer(a -> new RoleDTO());
    controller.create(null);
  }

  @Test
  public void should_create_with_body() throws Exception {
    RoleCreateForm form = new RoleCreateForm();
    when(service.create(any(RoleCreateForm.class))).thenAnswer(a -> new RoleDTO());
    controller.create(form);
    verify(service, times(1)).create(eq(form));
  }

  @Test
  public void should_create_with_body_exception() throws Exception {
    thrown.expect(Exception.class);

    RoleCreateForm form = new RoleCreateForm();
    when(service.create(any(RoleCreateForm.class))).thenThrow(new Exception());
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
      RoleDTO roleDTO = new RoleDTO();
      roleDTO.setId((Long) a.getArguments()[0]);
      return roleDTO;
    });
    ResponseEntity<RoleDTO> response = controller.get(id);
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
    ResponseEntity<RoleDTO> response = controller.get(id);
    verify(service, times(1)).getOne(eq(id));
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }


  @Test
  public void should_update_one_with_id_found() throws Exception {
    Long id = 1L;
    RoleUpdateForm form = new RoleUpdateForm();

    when(service.getOne(any(Long.class))).thenAnswer(a -> {
      RoleDTO roleDTO = new RoleDTO();
      roleDTO.setId((Long) a.getArguments()[0]);
      return roleDTO;
    });
    when(service.update(any(Long.class), any(RoleUpdateForm.class))).thenAnswer(a -> {
      RoleDTO roleDTO = new RoleDTO();
      roleDTO.setId((Long) a.getArguments()[0]);
      roleDTO.setName("test");
      return roleDTO;
    });

    ResponseEntity<RoleDTO> response = controller.update(id, form);
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
    ResponseEntity<RoleDTO> response = controller.update(id, new RoleUpdateForm());
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
    verify(service, times(0)).delete(any(RoleDTO.class), anyBoolean());
    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
  }

  @Test
  public void should_delete_one_with_id_found_without_associations() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new RoleDTO());
    when(service.delete(any(RoleDTO.class), anyBoolean())).thenReturn(Optional.empty());

    ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.delete(id, false);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(1)).delete(any(RoleDTO.class), anyBoolean());

    Assert.assertNotNull(response);
    Assert.assertNull(response.getBody());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
  }

  @Test
  public void should_delete_one_with_id_found_with_associations_no_force() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new RoleDTO());
    when(service.delete(any(RoleDTO.class), eq(false))).thenReturn(Optional.of(ImmutableMap.<Class<? extends AbstractDTO>, Long>builder().put(GroupDTO.class, 2L).put(UserDTO.class, 5L).build()));

    ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.delete(id, false);
    verify(service, times(1)).getOne(eq(id));
    verify(service, times(1)).delete(any(RoleDTO.class), anyBoolean());

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getBody());
    Assert.assertFalse(response.getBody().isEmpty());
    Assert.assertTrue(response.getStatusCode() == HttpStatus.CONFLICT);
  }

  @Test
  public void should_delete_one_with_id_found_with_associations_force() throws Exception {
    Long id = 1L;
    when(service.getOne(any(Long.class))).thenReturn(new RoleDTO());
    when(service.delete(any(RoleDTO.class), eq(false))).thenReturn(Optional.of(ImmutableMap.<Class<? extends AbstractDTO>, Long>builder().put(GroupDTO.class, 2L).put(UserDTO.class, 5L).build()));
    when(service.delete(any(RoleDTO.class), eq(true))).thenReturn(Optional.empty());

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
    verify(service, times(2)).delete(any(RoleDTO.class), anyBoolean());

  }
}
