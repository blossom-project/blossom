package com.blossomproject.ui.web.administration.membership;

import com.blossomproject.core.association_user_group.AssociationUserGroupDTO;
import com.blossomproject.core.association_user_group.AssociationUserGroupService;
import com.blossomproject.core.association_user_group.UpdateAssociationUserGroupForm;
import com.blossomproject.core.group.Group;
import com.blossomproject.core.group.GroupDTO;
import com.blossomproject.core.group.GroupService;
import com.blossomproject.core.user.User;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MembershipsControllerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private AssociationUserGroupService service;

    @Mock
    private UserService userService;

    @Mock
    private GroupService groupService;

    private MembershipsController controller;

    @Before
    public void setUp() {
        controller = new MembershipsController(service, userService, groupService);
    }

    @Test
    public void should_display_one_by_user_with_id_not_found() {
        Long id = 1L;
        when(userService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 1L));
        controller.getUserMembershipsPage(id, new ExtendedModelMap());
        verify(userService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_display_one_by_user_with_id_found() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(userService.getOne(any(Long.class))).thenReturn(userDTO);
        List<AssociationUserGroupDTO> associations = new ArrayList();
        AssociationUserGroupDTO association = new AssociationUserGroupDTO();
        association.setA(userDTO);
        associations.add(association);
        when(service.getAllLeft(any(UserDTO.class))).thenReturn(associations);
        ModelAndView modelAndView = controller.getUserMembershipsPage(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/memberships/memberships-user"));
        assertTrue(modelAndView.getModel().get("memberships") instanceof Page);
        verify(userService, times(1)).getOne(eq(1L));
        verify(service, times(1)).getAllLeft(eq(userDTO));
    }

    @Test
    public void should_display_form_user_id_not_found() {
        Long id = 1L;
        when(userService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 1L));
        controller.getUserMembershipsForm(id, new ExtendedModelMap());
        verify(userService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_display_form_user_with_id_found() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(userService.getOne(any(Long.class))).thenReturn(userDTO);
        List<AssociationUserGroupDTO> associations = new ArrayList();
        AssociationUserGroupDTO association = new AssociationUserGroupDTO();
        association.setA(userDTO);
        associations.add(association);
        when(service.getAllLeft(any(UserDTO.class))).thenReturn(associations);
        ModelAndView modelAndView = controller.getUserMembershipsForm(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/memberships/memberships-user-edit"));
        assertTrue(modelAndView.getModel().get("associations") instanceof List);
        assertTrue(modelAndView.getModel().get("users") instanceof List);
        assertTrue(modelAndView.getModel().get("groups") instanceof List);
        assertTrue(EqualsBuilder.reflectionEquals(userDTO, modelAndView.getModel().get("user")));
        assertNull(modelAndView.getModel().get("group"));
        verify(userService, times(1)).getOne(eq(1L));
        verify(service, times(1)).getAllLeft(eq(userDTO));
    }

    @Test
    public void should_update_user_with_id_not_found() {
        Long id = 1L;
        UpdateAssociationUserGroupForm updateAssociationUserGroupForm = new UpdateAssociationUserGroupForm();
        when(userService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 1L));
        controller.handleUserMembershipsForm(id, new ExtendedModelMap(), updateAssociationUserGroupForm);
        verify(userService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_update_user_with_id_found() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        UpdateAssociationUserGroupForm updateAssociationUserGroupForm = new UpdateAssociationUserGroupForm();

        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        updateAssociationUserGroupForm.setIds(ids);

        List<AssociationUserGroupDTO> associations = new ArrayList();

        when(service.getAllLeft(any(UserDTO.class))).thenReturn(associations);
        when(userService.getOne(any(Long.class))).thenReturn(userDTO);
        when(groupService.getAll(any(List.class))).thenReturn(Lists.newArrayList(new GroupDTO()));
        when(service.associate(any(UserDTO.class), any(GroupDTO.class))).thenReturn(null);

        ModelAndView modelAndView = controller.handleUserMembershipsForm(1L, new ExtendedModelMap(), updateAssociationUserGroupForm);
        assertTrue(modelAndView.getViewName().equals("blossom/memberships/memberships-user"));
        assertTrue(modelAndView.getModel().get("memberships") instanceof Page);
        verify(userService, times(1)).getOne(eq(1L));
        verify(service, times(2)).getAllLeft(eq(userDTO));
        verify(groupService, times(2)).getAll(any(List.class));
        verify(service, times(1)).associate(eq(userDTO), any(GroupDTO.class));
        verify(service, times(1)).dissociate(eq(userDTO), any(GroupDTO.class));
    }

    @Test
    public void should_display_one_by_group_with_id_not_found() {
        Long id = 1L;
        when(groupService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Group=%s not found", 1L));
        controller.getGroupMembershipsPage(id, new ExtendedModelMap());
        verify(groupService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_display_one_by_group_with_id_found() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        when(groupService.getOne(any(Long.class))).thenReturn(groupDTO);
        List<AssociationUserGroupDTO> associations = new ArrayList();
        when(service.getAllRight(any(GroupDTO.class))).thenReturn(associations);
        ModelAndView modelAndView = controller.getGroupMembershipsPage(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/memberships/memberships-group"));
        assertTrue(modelAndView.getModel().get("memberships") instanceof Page);
        verify(groupService, times(1)).getOne(eq(1L));
        verify(service, times(1)).getAllRight(eq(groupDTO));
    }

    @Test
    public void should_display_form_group_id_not_found() {
        Long id = 1L;
        when(groupService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Group=%s not found", 1L));
        controller.getGroupMembershipsForm(id, new ExtendedModelMap());
        verify(groupService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_display_form_group_with_id_found() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        when(groupService.getOne(any(Long.class))).thenReturn(groupDTO);
        List<AssociationUserGroupDTO> associations = new ArrayList();
        AssociationUserGroupDTO association = new AssociationUserGroupDTO();
        association.setB(groupDTO);
        associations.add(association);
        when(service.getAllRight(any(GroupDTO.class))).thenReturn(associations);
        ModelAndView modelAndView = controller.getGroupMembershipsForm(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/memberships/memberships-group-edit"));
        assertTrue(modelAndView.getModel().get("associations") instanceof List);
        assertTrue(modelAndView.getModel().get("users") instanceof List);
        assertTrue(modelAndView.getModel().get("groups") instanceof List);
        assertTrue(EqualsBuilder.reflectionEquals(groupDTO, modelAndView.getModel().get("group")));
        assertNull(modelAndView.getModel().get("user"));
        verify(groupService, times(1)).getOne(eq(1L));
        verify(service, times(1)).getAllRight(eq(groupDTO));
    }

    @Test
    public void should_update_group_with_id_not_found() {
        Long id = 1L;
        UpdateAssociationUserGroupForm updateAssociationUserGroupForm = new UpdateAssociationUserGroupForm();
        when(groupService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Group=%s not found", 1L));
        controller.handleGroupMembershipsForm(id, new ExtendedModelMap(), updateAssociationUserGroupForm);
        verify(groupService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_update_group_with_id_found() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        UpdateAssociationUserGroupForm updateAssociationUserGroupForm = new UpdateAssociationUserGroupForm();

        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        updateAssociationUserGroupForm.setIds(ids);

        List<AssociationUserGroupDTO> associations = new ArrayList();

        when(service.getAllRight(any(GroupDTO.class))).thenReturn(associations);
        when(groupService.getOne(any(Long.class))).thenReturn(groupDTO);
        when(userService.getAll(any(List.class))).thenReturn(Lists.newArrayList(new UserDTO()));
        when(service.associate(any(UserDTO.class), any(GroupDTO.class))).thenReturn(null);

        ModelAndView modelAndView = controller.handleGroupMembershipsForm(1L, new ExtendedModelMap(), updateAssociationUserGroupForm);
        assertTrue(modelAndView.getViewName().equals("blossom/memberships/memberships-group"));
        assertTrue(modelAndView.getModel().get("memberships") instanceof Page);
        verify(groupService, times(1)).getOne(eq(1L));
        verify(userService, times(2)).getAll(any(List.class));
        verify(service, times(2)).getAllRight(eq(groupDTO));
        verify(service, times(1)).associate(any(UserDTO.class), eq(groupDTO));
        verify(service, times(1)).dissociate(any(UserDTO.class), eq(groupDTO));
    }


}
