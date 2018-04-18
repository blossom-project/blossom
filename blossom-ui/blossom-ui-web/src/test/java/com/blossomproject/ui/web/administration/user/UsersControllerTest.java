package com.blossomproject.ui.web.administration.user;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SearchResult;
import com.blossomproject.core.group.GroupDTO;
import com.blossomproject.core.role.RoleDTO;
import com.blossomproject.core.user.User;
import com.blossomproject.core.user.UserCreateForm;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.blossomproject.core.user.UserUpdateForm;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.builder.EqualsBuilder;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RunWith(MockitoJUnitRunner.class)
public class UsersControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private UserService service;

    @Mock
    private SearchEngineImpl<UserDTO> searchEngine;

    @Mock
    private Tika tika;

    private UsersController controller;

    @Before
    public void setUp() {
        controller = new UsersController(service, searchEngine, tika);
    }

    @Test
    public void should_display_paged_users_without_query_parameter() throws Exception {
        when(service.getAll(any(Pageable.class))).thenAnswer(a -> new PageImpl<UserDTO>(Lists.newArrayList()));
        ModelAndView modelAndView = controller.getUsersPage(null, PageRequest.of(0, 20), null,  new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/users/users"));
        assertTrue(modelAndView.getModel().get("users") instanceof Page);
        verify(service, times(1)).getAll(any(Pageable.class));
    }

    @Test
    public void should_display_paged_users_with_query_parameter() throws Exception {
        when(searchEngine.search(any(String.class), any(Pageable.class), any(), anyList())).thenAnswer(a -> new SearchResult<>(0, new PageImpl<UserDTO>(Lists.newArrayList())));
        ModelAndView modelAndView = controller.getUsersPage("test", PageRequest.of(0, 10), null, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/users/users"));
        assertTrue(modelAndView.getModel().get("users") instanceof Page);
        verify(searchEngine, times(1)).search(eq("test"), any(Pageable.class), any(), anyList());
    }

    @Test
    public void should_display_create_page() throws Exception {
        UserCreateForm userCreateForm = new UserCreateForm();
        userCreateForm.setLocale(Locale.FRANCE);
        ModelAndView modelAndView = controller.getUserCreatePage(new ExtendedModelMap(), Locale.FRANCE);
        assertTrue(modelAndView.getViewName().equals("blossom/users/create"));
        assertTrue(EqualsBuilder.reflectionEquals(userCreateForm, modelAndView.getModel().get("userCreateForm")));
        assertTrue(EqualsBuilder.reflectionEquals(modelAndView.getModel().get("civilities"), User.Civility.values()));
    }

    @Test
    public void should_handle_create_without_form_error() throws Exception {
        UserCreateForm form = new UserCreateForm();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        UserDTO userToCreate = new UserDTO();
        userToCreate.setId(1L);
        when(service.create(any(UserCreateForm.class))).thenReturn(userToCreate);
        ModelAndView modelAndView = controller.handleUserCreateForm(form, result, new ExtendedModelMap());
        verify(service, times(1)).create(eq(form));
        assertTrue(modelAndView.getViewName().equals("redirect:../users/1"));
    }

    @Test
    public void should_handle_create_with_form_error() throws Exception {
        UserCreateForm form = new UserCreateForm();
        form.setFirstname("Firstname");
        form.setLastname("Lastname");
        form.setLocale(Locale.FRANCE);
        form.setCivility(User.Civility.MAN);
        form.setEmail("email@email.com");
        form.setIdentifier("test");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        ModelAndView modelAndView = controller.handleUserCreateForm(form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/users/create"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("userCreateForm")));
    }

    @Test
    public void should_handle_create_with_exception() throws Exception {
        UserCreateForm form = new UserCreateForm();
        form.setFirstname("Firstname");
        form.setLastname("Lastname");
        form.setLocale(Locale.FRANCE);
        form.setCivility(User.Civility.MAN);
        form.setEmail("email@email.com");
        form.setIdentifier("test");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(service.create(any(UserCreateForm.class))).thenThrow(new Exception());
        ModelAndView modelAndView = controller.handleUserCreateForm(form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/users/create"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("userCreateForm")));
    }

    @Test
    public void should_display_one_with_id_not_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 1L));
        controller.getUser(1L, new ExtendedModelMap(), req);
    }

    @Test
    public void should_display_one_with_id_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(userDTO);
        ModelAndView modelAndView = controller.getUser(1L, new ExtendedModelMap(), req);
        assertTrue(modelAndView.getViewName().equals("blossom/users/user"));
        assertTrue(EqualsBuilder.reflectionEquals(userDTO, modelAndView.getModel().get("user")));
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_display_one_informations_with_id_not_found() throws Exception {
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 1L));
        controller.getUserInformations(1L);
    }

    @Test
    public void should_display_one_informations_with_id_found() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(userDTO);
        ModelAndView modelAndView = controller.getUserInformations(1L);
        assertTrue(modelAndView.getViewName().equals("blossom/users/userinformations"));
        assertTrue(EqualsBuilder.reflectionEquals(userDTO, modelAndView.getModel().get("user")));
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_display_one_form_edit_with_id_not_found() throws Exception {
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 1L));
        controller.getUserInformationsForm(1L, new ExtendedModelMap());
    }

    @Test
    public void should_display_one_form_edit_with_id_found() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(userDTO);
        ModelAndView modelAndView = controller.getUserInformationsForm(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/users/userinformations-edit"));
        assertTrue(modelAndView.getStatus() == HttpStatus.OK);
        UserUpdateForm userUpdateForm = (UserUpdateForm) modelAndView.getModel().get("userUpdateForm");
        assertTrue(userUpdateForm.getId() == 1L);
        assertTrue(EqualsBuilder.reflectionEquals(modelAndView.getModel().get("civilities"), User.Civility.values()));
        assertTrue(EqualsBuilder.reflectionEquals(modelAndView.getModel().get("user"), userDTO));
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_handle_update_without_id_found() throws Exception {
        BindingResult result = mock(BindingResult.class);
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 1L));
        controller.handleUserInformationsUpdateForm(1L, new UserUpdateForm(), result, new ExtendedModelMap());

    }

    @Test
    public void should_handle_update_without_form_error() throws Exception {
        UserUpdateForm form = new UserUpdateForm();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        UserDTO userUpdated = new UserDTO();
        userUpdated.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(userUpdated);
        when(service.update(any(Long.class), any(UserUpdateForm.class))).thenReturn(userUpdated);
        ModelAndView modelAndView = controller.handleUserInformationsUpdateForm(1L, form, result, new ExtendedModelMap());
        verify(service, times(1)).update(eq(1L), eq(form));
        assertTrue(modelAndView.getViewName().equals("blossom/users/userinformations"));
        assertTrue(EqualsBuilder.reflectionEquals(userUpdated, modelAndView.getModel().get("user")));
    }

    @Test
    public void should_handle_update_with_form_error() throws Exception {
        UserUpdateForm form = new UserUpdateForm();
        form.setFirstname("Firstname");
        form.setLastname("Lastname");
        form.setCivility(User.Civility.MAN);
        form.setEmail("email@email.com");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(userDTO);
        ModelAndView modelAndView = controller.handleUserInformationsUpdateForm(1L, form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/users/userinformations-edit"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("userUpdateForm")));
        assertTrue(EqualsBuilder.reflectionEquals(userDTO, modelAndView.getModel().get("user")));
        assertTrue(EqualsBuilder.reflectionEquals(modelAndView.getModel().get("civilities"), User.Civility.values()));
        assertTrue(modelAndView.getStatus() == HttpStatus.CONFLICT);
    }

    @Test
    public void should_get_avatar_with_id_found() throws Exception {
        Long id = 1L;
        InputStream avatar = new ByteArrayInputStream("test".getBytes());
        when(service.loadAvatar(any(Long.class))).thenReturn(avatar);
        when(tika.detect(any(InputStream.class))).thenReturn("image/png");
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
        when(tika.detect(any(InputStream.class))).thenReturn("image/png");
        ResponseEntity<InputStreamResource> response = controller.displayAvatar(id);
        verify(service, times(1)).loadAvatar(eq(id));
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue((response.getBody().getInputStream().equals(defaultAvatar)));
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }


    @Test
    public void should_display_avatar_form_with_id_found() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(service.getOne(any(Long.class))).thenReturn(userDTO);
        ModelAndView modelAndView = controller.getUserAvatarForm(1L, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/users/useravatar-edit-modal"));
        assertTrue(EqualsBuilder.reflectionEquals(userDTO, modelAndView.getModel().get("user")));
        verify(service, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_display_avatar_form_with_id_not_found() throws Exception {
        when(service.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("User=%s not found", 1L));
        controller.getUserAvatarForm(1L, new ExtendedModelMap());
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

        controller.handleUserAvatarUpdateForm(id, multipartFile);
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

        controller.handleUserAvatarUpdateForm(id, multipartFile);
    }

    @Test
    public void should_delete_one_with_id_not_found() throws Exception {
        Long id = 1L;
        when(service.getOne(any(Long.class))).thenReturn(null);
        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteUser(id, false);
        verify(service, times(1)).getOne(eq(id));
        verify(service, times(0)).delete(any(UserDTO.class), anyBoolean());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void should_delete_one_with_id_found_without_associations() throws Exception {
        Long id = 1L;
        when(service.getOne(any(Long.class))).thenReturn(new UserDTO());
        when(service.delete(any(UserDTO.class), anyBoolean())).thenReturn(Optional.empty());

        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteUser(id, false);
        verify(service, times(1)).getOne(eq(id));
        verify(service, times(1)).delete(any(UserDTO.class), anyBoolean());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void should_delete_one_with_id_found_with_associations_no_force() throws Exception {
        Long id = 1L;
        when(service.getOne(any(Long.class))).thenReturn(new UserDTO());
        when(service.delete(any(UserDTO.class), eq(false))).thenReturn(Optional.of(ImmutableMap.<Class<? extends AbstractDTO>, Long>builder().put(GroupDTO.class, 2L).put(RoleDTO.class, 5L).build()));

        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteUser(id, false);
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
        when(service.delete(any(UserDTO.class), eq(true))).thenReturn(Optional.empty());

        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = controller.deleteUser(id, true);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);

        verify(service, times(1)).getOne(eq(id));
        verify(service, times(1)).delete(any(UserDTO.class), anyBoolean());
    }

}
