package com.blossomproject.ui.web.administration.role;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SearchResult;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import com.blossomproject.core.common.utils.tree.TreeNode;
import com.blossomproject.core.group.GroupDTO;
import com.blossomproject.core.role.*;
import com.blossomproject.core.user.UserDTO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RolesControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private RoleService service;

    @Mock
    private SearchEngineImpl<RoleDTO> searchEngine;

    @Mock
    private MessageSource messageSource;

    private RolesController controller;

    @Before
    public void setUp() {
        controller = new RolesController(service, searchEngine, messageSource);
    }

    @Test
    public void should_display_paged_roles_without_query_parameter() throws Exception {
        when(service.getAll(any(Pageable.class)))
                .thenAnswer(a -> new PageImpl<RoleDTO>(Lists.newArrayList()));
        ModelAndView modelAndView = controller.getRolesPage(null, PageRequest.of(0, 20), new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roles"));
        assertTrue(modelAndView.getModel().get("roles") instanceof Page);
        verify(service, times(1)).getAll(any(Pageable.class));
    }

    @Test
    public void should_display_paged_roles_with_query_parameter() throws Exception {
        when(searchEngine.search(any(String.class), any(Pageable.class)))
                .thenAnswer(a -> new SearchResult<>(0, new PageImpl<RoleDTO>(Lists.newArrayList())));
        ModelAndView modelAndView = controller.getRolesPage("test", PageRequest.of(0, 10), new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roles"));
        assertTrue(modelAndView.getModel().get("roles") instanceof Page);
        verify(searchEngine, times(1)).search(eq("test"), any(Pageable.class));
    }

    @Test
    public void should_display_create_page() throws Exception {
        RoleCreateForm roleCreateForm = new RoleCreateForm();
        ModelAndView modelAndView = controller.getRoleCreatePage(new ExtendedModelMap(), Locale.FRANCE);
        assertTrue(modelAndView.getViewName().equals("blossom/roles/create"));
        assertTrue(EqualsBuilder.reflectionEquals(roleCreateForm, modelAndView.getModel().get("roleCreateForm")));
    }

    @Test
    public void should_handle_create_without_form_error() throws Exception {
        RoleCreateForm form = new RoleCreateForm();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        RoleDTO roleToCreate = new RoleDTO();
        roleToCreate.setId(1L);
        when(service.create(any(RoleCreateForm.class))).thenReturn(roleToCreate);
        ModelAndView modelAndView = controller.handleRoleCreateForm(form, result, new ExtendedModelMap());
        verify(service, times(1)).create(eq(form));
        assertTrue(modelAndView.getViewName().equals("redirect:../roles/1"));
    }

    @Test
    public void should_handle_create_with_form_error() throws Exception {
        RoleCreateForm form = new RoleCreateForm();
        form.setDescription("Description");
        form.setName("Name");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        ModelAndView modelAndView = controller.handleRoleCreateForm(form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/roles/create"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("roleCreateForm")));
    }

    @Test
    public void should_handle_create_with_exception() throws Exception {
        RoleCreateForm form = new RoleCreateForm();
        form.setDescription("Description");
        form.setName("Name");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(service.create(any(RoleCreateForm.class))).thenThrow(new Exception());
        ModelAndView modelAndView = controller.handleRoleCreateForm(form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/roles/create"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("roleCreateForm")));
    }

    @Test
    public void should_display_one_with_id_not_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        controller.getRole(1L, new ExtendedModelMap(), req);
    }

    @Test
    public void should_display_one_with_id_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(roleDTO);
        ModelAndView modelAndView = controller.getRole(1L, new ExtendedModelMap(), req);
        assertTrue(modelAndView.getViewName().equals("blossom/roles/role"));
        assertTrue(EqualsBuilder.reflectionEquals(roleDTO, modelAndView.getModel().get("role")));
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_delete_one_with_id_not_found() throws Exception {
        Long id = 1L;
        when(service.getOne(any(Long.class))).thenReturn(null);
        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteRole(id, false);
        verify(service, times(1)).getOne(eq(id));
        verify(service, times(0)).delete(any(RoleDTO.class), anyBoolean());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void should_delete_one_with_id_found_without_associations() throws Exception {
        Long id = 1L;
        when(service.getOne(any(Long.class))).thenReturn(new RoleDTO());
        when(service.delete(any(RoleDTO.class), anyBoolean())).thenReturn(Optional.empty());

        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteRole(id, false);
        verify(service, times(1)).getOne(eq(id));
        verify(service, times(1)).delete(any(RoleDTO.class), anyBoolean());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void should_delete_one_with_id_found_with_associations_no_force() throws Exception {
        Long id = 1L;
        when(service.getOne(any(Long.class))).thenReturn(new RoleDTO());
        when(service.delete(any(RoleDTO.class), eq(false))).thenReturn(Optional.of(ImmutableMap.<Class<? extends AbstractDTO>, Long>builder().put(GroupDTO.class, 2L).put(UserDTO.class, 5L).build()));

        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteRole(id, false);
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
        when(service.delete(any(RoleDTO.class), eq(true))).thenReturn(Optional.empty());

        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteRole(id, true);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);

        verify(service, times(1)).getOne(eq(id));
        verify(service, times(1)).delete(any(RoleDTO.class), anyBoolean());

    }

    @Test
    public void should_display_one_informations_with_id_not_found() throws Exception {
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        controller.getRoleInformations(1L);
    }

    @Test
    public void should_display_one_informations_with_id_found() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(roleDTO);
        ModelAndView modelAndView = controller.getRoleInformations(1L);
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roleinformations"));
        assertTrue(EqualsBuilder.reflectionEquals(roleDTO, modelAndView.getModel().get("role")));
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_display_one_form_edit_with_id_not_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        controller.getRoleInformationsForm(1L, new ExtendedModelMap(), req);
    }

    @Test
    public void should_display_one_form_edit_with_id_found() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(service.getOne(any(Long.class))).thenReturn(roleDTO);
        ModelAndView modelAndView = controller.getRoleInformationsForm(1L, new ExtendedModelMap(), req);
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roleinformations-edit"));
        assertTrue(modelAndView.getStatus() == HttpStatus.OK);
        RoleUpdateForm roleUpdateForm = (RoleUpdateForm) modelAndView.getModel().get("roleUpdateForm");
        assertTrue(roleUpdateForm.getId() == 1L);
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_handle_update_without_id_found() throws Exception {
        BindingResult result = mock(BindingResult.class);
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        ModelAndView modelAndView = controller.handleRoleInformationsForm(1L, new ExtendedModelMap(), new RoleUpdateForm(), result);
    }

    @Test
    public void should_handle_update_without_form_error() throws Exception {
        RoleUpdateForm form = new RoleUpdateForm();
        form.setDescription("Description");
        form.setName("Name");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        RoleDTO roleUpdated = new RoleDTO();
        roleUpdated.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(roleUpdated);
        when(service.update(any(Long.class), any(RoleDTO.class))).thenReturn(roleUpdated);
        ModelAndView modelAndView = controller.handleRoleInformationsForm(1L, new ExtendedModelMap(), form, result);
        verify(service, times(1)).update(eq(1L), eq(roleUpdated));
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roleinformations"));
        assertTrue(EqualsBuilder.reflectionEquals(roleUpdated, modelAndView.getModel().get("role")));
    }

    @Test
    public void should_handle_update_with_form_error() throws Exception {
        RoleUpdateForm form = new RoleUpdateForm();
        form.setDescription("Description");
        form.setName("Name");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        ModelAndView modelAndView = controller.handleRoleInformationsForm(1L, new ExtendedModelMap(), form, result);
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roleinformations-edit"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("roleUpdateForm")));
        assertTrue(modelAndView.getStatus() == HttpStatus.CONFLICT);
    }

    @Test
    public void should_get_privilege_tree_node_json() {
        when(messageSource.getMessage(any(String.class), any(), any(String.class), any(Locale.class))).thenReturn("Test");

        List<Privilege> privileges = new ArrayList();
        privileges.add(new SimplePrivilege("administration:users:write"));
        privileges.add(new SimplePrivilege("administration:users:delete"));
        when(service.getAvailablePrivileges()).thenReturn(privileges);
        assertTrue(controller.privilegeTreeNode(Locale.ENGLISH) instanceof TreeNode);
    }

    @Test
    public void should_display_one_privileges_with_id_not_found() throws Exception {
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        controller.getRolePrivileges(1L, new ExtendedModelMap());
    }

    @Test
    public void should_display_one_privileges_with_id_found() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(roleDTO);
        ModelAndView modelAndView = controller.getRolePrivileges(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roleprivileges"));
        assertTrue(EqualsBuilder.reflectionEquals(roleDTO, modelAndView.getModel().get("role")));
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_display_one_privileges_form_edit_with_id_not_found() throws Exception {
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        controller.getRolePrivilegesForm(1L, new ExtendedModelMap());
    }

    @Test
    public void should_display_one_privileges_form_edit_with_id_found() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(roleDTO);
        ModelAndView modelAndView = controller.getRolePrivilegesForm(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roleprivileges-edit"));
        RolePrivilegeUpdateForm rolePrivilegeUpdateForm = (RolePrivilegeUpdateForm) modelAndView.getModel().get("rolePrivilegeUpdateForm");
        assertTrue(rolePrivilegeUpdateForm instanceof RolePrivilegeUpdateForm);
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_handle_update_privileges_without_id_found() throws Exception {
        BindingResult result = mock(BindingResult.class);
        when(service.getOne(any(Long.class))).thenReturn(null);
        List<Privilege> privileges = new ArrayList();
        privileges.add(new SimplePrivilege("administration:users:delete"));
        when(service.getAvailablePrivileges()).thenReturn(privileges);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        controller.handleRolePrivilegesForm(1L, new RolePrivilegeUpdateForm(), result, new ExtendedModelMap());
    }

    @Test
    public void should_handle_update_privileges_without_form_error() throws Exception {
        RolePrivilegeUpdateForm form = new RolePrivilegeUpdateForm();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        RoleDTO roleUpdated = new RoleDTO();
        roleUpdated.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(roleUpdated);
        when(service.update(any(Long.class), any(RoleDTO.class))).thenReturn(roleUpdated);
        ModelAndView modelAndView = controller.handleRolePrivilegesForm(1L, form, result, new ExtendedModelMap());
        verify(service, times(1)).update(eq(1L), eq(roleUpdated));
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roleprivileges"));
        assertTrue(EqualsBuilder.reflectionEquals(roleUpdated, modelAndView.getModel().get("role")));
    }

    @Test
    public void should_handle_update_privileges_with_form_error() throws Exception {
        RolePrivilegeUpdateForm form = new RolePrivilegeUpdateForm();
        List<String> privileges = new ArrayList();
        privileges.add("administration:users:write");
        privileges.add("administration:users:delete");
        form.setPrivileges(privileges);
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        ModelAndView modelAndView = controller.handleRolePrivilegesForm(1L, form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/roles/roleprivileges-edit"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("rolePrivilegeUpdateForm")));
    }
}
