package com.blossomproject.ui.web.administration.group;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SearchResult;
import com.blossomproject.core.group.*;
import com.blossomproject.core.role.RoleDTO;
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
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GroupsControllerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private GroupService service;

    @Mock
    private SearchEngineImpl<GroupDTO> searchEngine;

    private GroupsController controller;

    @Before
    public void setUp() {
        controller = new GroupsController(service, searchEngine);
    }

    @Test
    public void should_display_paged_groups_without_query_parameter() throws Exception {
        when(service.getAll(any(Pageable.class)))
                .thenAnswer(a -> new PageImpl<GroupDTO>(Lists.newArrayList()));
        ModelAndView modelAndView = controller.getGroupsPage(null, PageRequest.of(0, 20), new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/groups/groups"));
        assertTrue(modelAndView.getModel().get("groups") instanceof Page);
        verify(service, times(1)).getAll(any(Pageable.class));
    }

    @Test
    public void should_display_paged_groups_with_query_parameter() throws Exception {
        when(searchEngine.search(any(String.class), any(Pageable.class)))
                .thenAnswer(a -> new SearchResult<>(0, new PageImpl<GroupDTO>(Lists.newArrayList())));
        ModelAndView modelAndView = controller.getGroupsPage("test", PageRequest.of(0, 10), new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/groups/groups"));
        assertTrue(modelAndView.getModel().get("groups") instanceof Page);
        verify(searchEngine, times(1)).search(eq("test"), any(Pageable.class));
    }

    @Test
    public void should_display_create_page() throws Exception {
        GroupCreateForm groupCreateForm = new GroupCreateForm();
        groupCreateForm.setLocale(Locale.FRANCE);
        ModelAndView modelAndView = controller.getGroupCreateForm(new ExtendedModelMap(), Locale.FRANCE);
        assertTrue(modelAndView.getViewName().equals("blossom/groups/create"));
        assertTrue(EqualsBuilder.reflectionEquals(groupCreateForm, modelAndView.getModel().get("groupCreateForm")));
    }

    @Test
    public void should_handle_create_without_form_error() throws Exception {
        GroupCreateForm form = new GroupCreateForm();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        GroupDTO groupToCreate = new GroupDTO();
        groupToCreate.setId(1L);
        when(service.create(any(GroupCreateForm.class))).thenReturn(groupToCreate);
        ModelAndView modelAndView = controller.handleGroupCreateForm(form, result, new ExtendedModelMap());
        verify(service, times(1)).create(eq(form));
        assertTrue(modelAndView.getViewName().equals("redirect:../groups/1"));
    }

    @Test
    public void should_handle_create_with_form_error() throws Exception {
        GroupCreateForm form = new GroupCreateForm();
        form.setName("Name");
        form.setDescription("Descr");
        form.setLocale(Locale.FRANCE);
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        ModelAndView modelAndView = controller.handleGroupCreateForm(form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/groups/create"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("groupCreateForm")));
    }

    @Test
    public void should_handle_create_with_exception() throws Exception {
        GroupCreateForm form = new GroupCreateForm();
        form.setName("Name");
        form.setDescription("Descr");
        form.setLocale(Locale.FRANCE);
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(service.create(any(GroupCreateForm.class))).thenThrow(new Exception());
        ModelAndView modelAndView = controller.handleGroupCreateForm(form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/groups/create"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("groupCreateForm")));
    }

    @Test
    public void should_display_one_with_id_not_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Group=%s not found", 1L));
        controller.getGroup(1L, new ExtendedModelMap(), req);
    }

    @Test
    public void should_display_one_with_id_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(groupDTO);
        ModelAndView modelAndView = controller.getGroup(1L, new ExtendedModelMap(), req);
        assertTrue(modelAndView.getViewName().equals("blossom/groups/group"));
        assertTrue(EqualsBuilder.reflectionEquals(groupDTO, modelAndView.getModel().get("group")));
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_delete_one_with_id_not_found() throws Exception {
        Long id = 1L;
        when(service.getOne(any(Long.class))).thenReturn(null);
        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteGroup(id, false);
        verify(service, times(1)).getOne(eq(id));
        verify(service, times(0)).delete(any(GroupDTO.class), anyBoolean());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void should_delete_one_with_id_found_without_associations() throws Exception {
        Long id = 1L;
        when(service.getOne(any(Long.class))).thenReturn(new GroupDTO());
        when(service.delete(any(GroupDTO.class), anyBoolean())).thenReturn(Optional.empty());

        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteGroup(id, false);
        verify(service, times(1)).getOne(eq(id));
        verify(service, times(1)).delete(any(GroupDTO.class), anyBoolean());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void should_delete_one_with_id_found_with_associations_no_force() throws Exception {
        Long id = 1L;
        when(service.getOne(any(Long.class))).thenReturn(new GroupDTO());
        when(service.delete(any(GroupDTO.class), eq(false))).thenReturn(Optional.of(
                ImmutableMap.<Class<? extends AbstractDTO>, Long>builder().put(RoleDTO.class, 2L)
                        .put(UserDTO.class, 5L).build()));

        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteGroup(id, false);
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
        when(service.delete(any(GroupDTO.class), eq(true))).thenReturn(Optional.empty());

        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response =  controller.deleteGroup(id, true);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);

        verify(service, times(1)).getOne(eq(id));
        verify(service, times(1)).delete(any(GroupDTO.class), anyBoolean());
    }

    @Test
    public void should_display_one_informations_with_id_not_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Group=%s not found", 1L));
        controller.getGroupInformations(1L, req);
    }

    @Test
    public void should_display_one_informations_with_id_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(groupDTO);
        ModelAndView modelAndView = controller.getGroupInformations(1L, req);
        assertTrue(modelAndView.getViewName().equals("blossom/groups/groupinformations"));
        assertTrue(EqualsBuilder.reflectionEquals(groupDTO, modelAndView.getModel().get("group")));
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_display_one_form_edit_with_id_not_found() throws Exception {
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Group=%s not found", 1L));
        controller.getGroupInformationsForm(1L,new ExtendedModelMap());
    }

    @Test
    public void should_display_one_form_edit_with_id_found() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(groupDTO);
        ModelAndView modelAndView = controller.getGroupInformationsForm(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/groups/groupinformations-edit"));
        assertTrue(modelAndView.getStatus() == HttpStatus.OK);
        GroupUpdateForm groupUpdateForm = (GroupUpdateForm) modelAndView.getModel().get("groupUpdateForm");
        assertTrue(groupUpdateForm.getId() == 1L);
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_handle_update_without_id_found() throws Exception {
        BindingResult result = mock(BindingResult.class);
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Group=%s not found", 1L));
        controller.handleGroupInformationsForm(1L, new ExtendedModelMap(), new GroupUpdateForm(), result);

    }

    @Test
    public void should_handle_update_without_form_error() throws Exception {
        GroupUpdateForm form = new GroupUpdateForm();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        GroupDTO groupUpdated = new GroupDTO();
        groupUpdated.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(groupUpdated);
        when(service.update(any(Long.class), any(GroupDTO.class))).thenReturn(groupUpdated);
        ModelAndView modelAndView = controller.handleGroupInformationsForm(1L, new ExtendedModelMap(), form, result);
        verify(service, times(1)).update(eq(1L), eq(groupUpdated));
        assertTrue(modelAndView.getViewName().equals("blossom/groups/groupinformations"));
        assertTrue(EqualsBuilder.reflectionEquals(groupUpdated, modelAndView.getModel().get("group")));
    }

    @Test
    public void should_handle_update_with_form_error() throws Exception {
        GroupUpdateForm form = new GroupUpdateForm();
        form.setName("Name");
        form.setDescription("Descr");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        ModelAndView modelAndView = controller.handleGroupInformationsForm(1L, new ExtendedModelMap(), form, result);
        assertTrue(modelAndView.getViewName().equals("blossom/groups/groupinformations-edit"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("groupUpdateForm")));
        assertTrue(modelAndView.getStatus() == HttpStatus.CONFLICT);
    }
}
