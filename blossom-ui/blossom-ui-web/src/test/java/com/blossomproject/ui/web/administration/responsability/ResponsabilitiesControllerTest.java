package com.blossomproject.ui.web.administration.responsability;

import com.blossomproject.core.association_user_role.AssociationUserRoleDTO;
import com.blossomproject.core.association_user_role.AssociationUserRoleService;
import com.blossomproject.core.association_user_role.UpdateAssociationUserRoleForm;
import com.blossomproject.core.role.Role;
import com.blossomproject.core.role.RoleDTO;
import com.blossomproject.core.role.RoleService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResponsabilitiesControllerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private AssociationUserRoleService service;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    private ResponsabilitiesController controller;

    @Before
    public void setUp() {
        controller = new ResponsabilitiesController(userService, service, roleService);
    }

    @Test
    public void should_display_one_by_user_with_id_not_found() {
        Long id = 2L;
        when(userService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 2L));
        controller.getUserMembershipsPage(id, new ExtendedModelMap());
        verify(userService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_display_one_by_user_with_id_found() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(userService.getOne(any(Long.class))).thenReturn(userDTO);
        List<AssociationUserRoleDTO> associations = new ArrayList();
        AssociationUserRoleDTO association = new AssociationUserRoleDTO();
        association.setA(userDTO);
        associations.add(association);
        when(service.getAllLeft(any(UserDTO.class))).thenReturn(associations);
        ModelAndView modelAndView = controller.getUserMembershipsPage(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/responsabilities/responsabilities-user"));
        assertTrue(modelAndView.getModel().get("responsabilities") instanceof Page);
        verify(userService, times(1)).getOne(eq(1L));
        verify(service, times(1)).getAllLeft(eq(userDTO));
    }

    @Test
    public void should_display_form_user_id_not_found() {
        Long id = 2L;
        when(userService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 2L));
        controller.getUserMembershipsForm(id, new ExtendedModelMap());
        verify(userService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_display_form_user_with_id_found() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(userService.getOne(any(Long.class))).thenReturn(userDTO);
        List<AssociationUserRoleDTO> associations = new ArrayList();
        AssociationUserRoleDTO association = new AssociationUserRoleDTO();
        association.setA(userDTO);
        associations.add(association);
        when(service.getAllLeft(any(UserDTO.class))).thenReturn(associations);
        ModelAndView modelAndView = controller.getUserMembershipsForm(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/responsabilities/responsabilities-user-edit"));
        assertTrue(modelAndView.getModel().get("associations") instanceof List);
        assertTrue(modelAndView.getModel().get("users") instanceof List);
        assertTrue(modelAndView.getModel().get("roles") instanceof List);
        assertTrue(EqualsBuilder.reflectionEquals(userDTO, modelAndView.getModel().get("user")));
        assertNull(modelAndView.getModel().get("role"));
        verify(userService, times(1)).getOne(eq(1L));
        verify(service, times(1)).getAllLeft(eq(userDTO));
    }

    @Test
    public void should_update_user_with_id_not_found() {
        Long id = 1L;
        UpdateAssociationUserRoleForm updateAssociationUserRoleForm = new UpdateAssociationUserRoleForm();
        when(userService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 1L));
        controller.handleUserMembershipsForm(id, new ExtendedModelMap(), updateAssociationUserRoleForm);
        verify(userService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_update_user_with_id_found() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        UpdateAssociationUserRoleForm updateAssociationUserRoleForm = new UpdateAssociationUserRoleForm();

        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        updateAssociationUserRoleForm.setIds(ids);

        List<AssociationUserRoleDTO> associations = new ArrayList();

        when(service.getAllLeft(any(UserDTO.class))).thenReturn(associations);
        when(userService.getOne(any(Long.class))).thenReturn(userDTO);
        when(roleService.getAll(any(List.class))).thenReturn(Lists.newArrayList(new RoleDTO()));
        when(service.associate(any(UserDTO.class), any(RoleDTO.class))).thenReturn(null);

        ModelAndView modelAndView = controller.handleUserMembershipsForm(1L, new ExtendedModelMap(), updateAssociationUserRoleForm);
        assertTrue(modelAndView.getViewName().equals("blossom/responsabilities/responsabilities-user"));
        assertTrue(modelAndView.getModel().get("responsabilities") instanceof Page);
        verify(userService, times(1)).getOne(eq(1L));
        verify(service, times(2)).getAllLeft(eq(userDTO));
        verify(roleService, times(2)).getAll(any(List.class));
        verify(service, times(1)).associate(eq(userDTO), any(RoleDTO.class));
        verify(service, times(1)).dissociate(eq(userDTO), any(RoleDTO.class));
    }

    @Test
    public void should_display_one_by_role_with_id_not_found() {
        Long id = 1L;
        when(roleService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        controller.getRoleMembershipsPage(id, new ExtendedModelMap());
        verify(roleService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_display_one_by_role_with_id_found() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        when(roleService.getOne(any(Long.class))).thenReturn(roleDTO);
        List<AssociationUserRoleDTO> associations = new ArrayList();
        AssociationUserRoleDTO association = new AssociationUserRoleDTO();
        association.setB(roleDTO);
        associations.add(association);
        when(service.getAllRight(any(RoleDTO.class))).thenReturn(associations);
        ModelAndView modelAndView = controller.getRoleMembershipsPage(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/responsabilities/responsabilities-role"));
        assertTrue(modelAndView.getModel().get("responsabilities") instanceof Page);
        verify(roleService, times(1)).getOne(eq(1L));
        verify(service, times(1)).getAllRight(eq(roleDTO));
    }

    @Test
    public void should_display_form_role_with_id_not_found() {
        Long id = 1L;
        when(roleService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        controller.getRoleMembershipsForm(id, new ExtendedModelMap());
        verify(roleService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_display_form_role_with_id_found() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        when(roleService.getOne(any(Long.class))).thenReturn(roleDTO);
        List<AssociationUserRoleDTO> associations = new ArrayList();
        AssociationUserRoleDTO association = new AssociationUserRoleDTO();
        association.setB(roleDTO);
        associations.add(association);
        when(service.getAllRight(any(RoleDTO.class))).thenReturn(associations);
        ModelAndView modelAndView = controller.getRoleMembershipsForm(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/responsabilities/responsabilities-role-edit"));
        assertTrue(modelAndView.getModel().get("associations") instanceof List);
        assertTrue(modelAndView.getModel().get("users") instanceof List);
        assertTrue(modelAndView.getModel().get("roles") instanceof List);
        assertTrue(EqualsBuilder.reflectionEquals(roleDTO, modelAndView.getModel().get("role")));
        assertNull(modelAndView.getModel().get("user"));
        verify(roleService, times(1)).getOne(eq(1L));
        verify(service, times(1)).getAllRight(eq(roleDTO));
    }

    @Test
    public void should_update_role_with_id_not_found() {
        Long id = 1L;
        UpdateAssociationUserRoleForm updateAssociationUserRoleForm = new UpdateAssociationUserRoleForm();
        when(roleService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Role=%s not found", 1L));
        controller.handleRoleResponsabilitiesForm(id, new ExtendedModelMap(), updateAssociationUserRoleForm);
        verify(roleService, times(1)).getOne(eq(id));
    }

    @Test
    public void should_update_role_with_id_found() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        UpdateAssociationUserRoleForm updateAssociationUserRoleForm = new UpdateAssociationUserRoleForm();

        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        updateAssociationUserRoleForm.setIds(ids);

        List<AssociationUserRoleDTO> associations = new ArrayList();

        when(service.getAllRight(any(RoleDTO.class))).thenReturn(associations);
        when(roleService.getOne(any(Long.class))).thenReturn(roleDTO);
        when(userService.getAll(any(List.class))).thenReturn(Lists.newArrayList(new UserDTO()));
        when(service.associate(any(UserDTO.class), any(RoleDTO.class))).thenReturn(null);

        ModelAndView modelAndView = controller.handleRoleResponsabilitiesForm(1L, new ExtendedModelMap(), updateAssociationUserRoleForm);
        assertTrue(modelAndView.getViewName().equals("blossom/responsabilities/responsabilities-role"));
        assertTrue(modelAndView.getModel().get("responsabilities") instanceof Page);
        verify(roleService, times(1)).getOne(eq(1L));
        verify(service, times(2)).getAllRight(eq(roleDTO));
        verify(userService, times(2)).getAll(any(List.class));
        verify(service, times(1)).associate(any(UserDTO.class), eq(roleDTO));
        verify(service, times(1)).dissociate(any(UserDTO.class), eq(roleDTO));
    }
}
