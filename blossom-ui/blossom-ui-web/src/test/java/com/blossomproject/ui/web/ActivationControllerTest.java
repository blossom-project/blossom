package com.blossomproject.ui.web;

import com.blossomproject.core.common.utils.action_token.ActionToken;
import com.blossomproject.core.common.utils.action_token.ActionTokenService;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActivationControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    ActionTokenService tokenService;

    @Mock
    UserService userService;

    ActivationController controller;

    @Before
    public void setUp() {
        controller = new ActivationController(tokenService, userService);
    }

    @Test
    public void activate_should_redirect_home_if_exception() throws Exception {
        when(tokenService.decryptToken(anyString())).thenAnswer(token -> {
            throw new Exception();
        });
        String result = controller.activateAccount("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertTrue("redirect:/blossom".equals(result));
        verify(tokenService, times(1)).decryptToken(anyString());
    }

    @Test
    public void activate_should_redirect_home_if_actionToken_invalid() throws Exception {
        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(false);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);
        String result = controller.activateAccount("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertTrue("redirect:/blossom".equals(result));
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
    }

    @Test
    public void activate_should_redirect_home_if_actionToken_action_not_activation() throws Exception {
        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(true);
        when(actionToken.getAction()).thenReturn(UserService.USER_RESET_PASSWORD);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);
        String result = controller.activateAccount("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertTrue("redirect:/blossom".equals(result));
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        verify(actionToken, times(1)).getAction();
    }

    @Test
    public void activate_should_redirect_home_if_user_not_present() throws Exception {
        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(true);
        when(actionToken.getAction()).thenReturn(UserService.USER_ACTIVATION);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);
        when(userService.getByActionToken(any(ActionToken.class))).thenReturn(Optional.empty());
        String result = controller.activateAccount("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertTrue("redirect:/blossom".equals(result));
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        verify(actionToken, times(1)).getAction();
        verify(userService, times(1)).getByActionToken(eq(actionToken));
    }

    @Test
    public void activate_should_redirect_change_password_if_user_present() throws Exception {
        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(true);
        when(actionToken.getAction()).thenReturn(UserService.USER_ACTIVATION);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(userService.getByActionToken(any(ActionToken.class))).thenReturn(Optional.of(userDTO));
        when(userService.generatePasswordResetToken(any(UserDTO.class))).thenReturn("test_token");
        String result = controller.activateAccount("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertTrue("redirect:/blossom/public/change_password?token=test_token".equals(result));
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        verify(actionToken, times(1)).getAction();
        verify(userService, times(1)).getByActionToken(eq(actionToken));
        verify(userService, times(1)).updateActivation(eq(userDTO.getId()), eq(true));
        verify(userService, times(1)).generatePasswordResetToken(eq(userDTO));
    }

    @Test
    public void reset_password_should_redirect_home_if_exception() throws Exception {
        when(tokenService.decryptToken(anyString())).thenAnswer(token -> {
            throw new Exception();
        });
        ModelAndView result = controller.resetPassword("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        verify(tokenService, times(1)).decryptToken(anyString());
        Assert.assertEquals("/blossom", ((RedirectView) result.getView()).getUrl());
    }

    @Test
    public void reset_password_should_redirect_home_if_actionToken_invalid() throws Exception {
        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(false);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);
        ModelAndView result = controller.resetPassword("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        Assert.assertEquals("/blossom", ((RedirectView) result.getView()).getUrl());
    }

    @Test
    public void reset_password_should_redirect_home_if_actionToken_action_not_reset_password() throws Exception {
        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(true);
        when(actionToken.getAction()).thenReturn(UserService.USER_ACTIVATION);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);
        ModelAndView result = controller.resetPassword("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        verify(actionToken, times(1)).getAction();
        Assert.assertEquals("/blossom", ((RedirectView) result.getView()).getUrl());
    }

    @Test
    public void reset_password_should_redirect_home_if_user_not_present() throws Exception {
        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(true);
        when(actionToken.getAction()).thenReturn(UserService.USER_RESET_PASSWORD);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);
        when(userService.getByActionToken(any(ActionToken.class))).thenReturn(Optional.empty());
        ModelAndView result = controller.resetPassword("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        verify(actionToken, times(1)).getAction();
        verify(userService, times(1)).getByActionToken(eq(actionToken));
        Assert.assertEquals("/blossom", ((RedirectView) result.getView()).getUrl());
    }

    @Test
    public void reset_password_should_redirect_change_password_if_user_present() throws Exception {
        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(true);
        when(actionToken.getAction()).thenReturn(UserService.USER_RESET_PASSWORD);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(userService.getByActionToken(any(ActionToken.class))).thenReturn(Optional.of(userDTO));
        ModelAndView result = controller.resetPassword("test", new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertTrue("blossom/activation/change-password".equals(result.getViewName()));
        Assert.assertNotNull(result.getModel());
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        verify(actionToken, times(1)).getAction();
        verify(userService, times(1)).getByActionToken(eq(actionToken));

        ModelAndView modelAndView = new ModelAndView(new RedirectView("/blossom"));
    }

    @Test
    public void change_password_should_display_change_password_if_binding_errors() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        ActivationController.UpdatePasswordForm updatePasswordForm = new ActivationController.UpdatePasswordForm();
        ModelAndView result = controller.changePassword(new ExtendedModelMap(), updatePasswordForm, bindingResult);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getViewName(), "blossom/activation/change-password");
        Assert.assertTrue(result.getModel().containsKey("updatePasswordForm"));
        Assert.assertTrue(result.getModel().containsValue(updatePasswordForm));
        verify(bindingResult, times(1)).hasErrors();
    }

    @Test
    public void change_password_should_redirect_home_if_exception() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        ActivationController.UpdatePasswordForm updatePasswordForm = new ActivationController.UpdatePasswordForm();
        updatePasswordForm.setToken("test");
        when(tokenService.decryptToken(anyString())).thenAnswer(token -> {
            throw new Exception();
        });
        ModelAndView result = controller.changePassword(new ExtendedModelMap(), updatePasswordForm, bindingResult);
        Assert.assertNotNull(result);
        verify(tokenService, times(1)).decryptToken(anyString());
        Assert.assertEquals("/blossom", ((RedirectView) result.getView()).getUrl());
    }

    @Test
    public void change_password_should_redirect_home_if_actionToken_invalid() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ActivationController.UpdatePasswordForm updatePasswordForm = new ActivationController.UpdatePasswordForm();
        updatePasswordForm.setToken("test");

        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(false);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);

        ModelAndView result = controller.changePassword(new ExtendedModelMap(), updatePasswordForm, bindingResult);
        Assert.assertNotNull(result);
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        Assert.assertEquals("/blossom", ((RedirectView) result.getView()).getUrl());
    }

    @Test
    public void change_password_should_redirect_home_if_actionToken_action_not_reset_password() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ActivationController.UpdatePasswordForm updatePasswordForm = new ActivationController.UpdatePasswordForm();
        updatePasswordForm.setToken("test");

        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(true);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);
        when(actionToken.getAction()).thenReturn(UserService.USER_ACTIVATION);

        ModelAndView result = controller.changePassword(new ExtendedModelMap(), updatePasswordForm, bindingResult);
        Assert.assertNotNull(result);
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        verify(actionToken, times(1)).getAction();
        Assert.assertEquals("/blossom", ((RedirectView) result.getView()).getUrl());
    }

    @Test
    public void change_password_should_redirect_home_if_user_not_present() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ActivationController.UpdatePasswordForm updatePasswordForm = new ActivationController.UpdatePasswordForm();
        updatePasswordForm.setToken("test");

        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(true);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);
        when(actionToken.getAction()).thenReturn(UserService.USER_RESET_PASSWORD);
        when(userService.getByActionToken(any(ActionToken.class))).thenReturn(Optional.empty());

        ModelAndView result = controller.changePassword(new ExtendedModelMap(), updatePasswordForm, bindingResult);
        Assert.assertNotNull(result);
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        verify(actionToken, times(1)).getAction();
        verify(userService, times(1)).getByActionToken(eq(actionToken));
        Assert.assertEquals("/blossom", ((RedirectView) result.getView()).getUrl());
    }

    @Test
    public void change_password_should_redirect_home_if_user_present() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ActivationController.UpdatePasswordForm updatePasswordForm = new ActivationController.UpdatePasswordForm();
        updatePasswordForm.setToken("test");
        updatePasswordForm.setPassword("pwd");

        ActionToken actionToken = mock(ActionToken.class);
        when(actionToken.isValid()).thenReturn(true);
        when(tokenService.decryptToken(anyString())).thenReturn(actionToken);
        when(actionToken.getAction()).thenReturn(UserService.USER_RESET_PASSWORD);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(userService.getByActionToken(any(ActionToken.class))).thenReturn(Optional.of(userDTO));

        ModelAndView result = controller.changePassword(new ExtendedModelMap(), updatePasswordForm, bindingResult);
        Assert.assertNotNull(result);
        verify(tokenService, times(1)).decryptToken(anyString());
        verify(actionToken, times(1)).isValid();
        verify(actionToken, times(1)).getAction();
        verify(userService, times(1)).getByActionToken(eq(actionToken));
        verify(userService, times(1)).updatePassword(eq(userDTO.getId()), eq(updatePasswordForm.getPassword()));
        Assert.assertEquals("/blossom", ((RedirectView) result.getView()).getUrl());
    }

    @Test
    public void should_display_ask_for_forgotten_password() throws Exception {
        ModelAndView result = controller.askForForgottenPassword();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getViewName(), "blossom/activation/ask-password");
        Assert.assertTrue(result.getModel().get("askPasswordForm") instanceof ActivationController.AskPasswordForm);
    }

    @Test
    public void should_display_ask_for_forgotten_password_if_binding_errors() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ActivationController.AskPasswordForm askPasswordForm = new ActivationController.AskPasswordForm();

        ModelAndView result = controller.askForForgottenPassword(askPasswordForm, bindingResult, new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getViewName(), "blossom/activation/ask-password");
        Assert.assertTrue(result.getModel().isEmpty());
    }

    @Test
    public void should_display_ask_for_forgotten_password_if_user_not_present() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ActivationController.AskPasswordForm askPasswordForm = new ActivationController.AskPasswordForm();
        askPasswordForm.setLoginOrEmail("test@test.fr");

        when(userService.getByIdentifier(eq(askPasswordForm.getLoginOrEmail()))).thenReturn(Optional.empty());
        when(userService.getByEmail(eq(askPasswordForm.getLoginOrEmail()))).thenReturn(Optional.empty());

        ModelAndView result = controller.askForForgottenPassword(askPasswordForm, bindingResult, new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getViewName(), "blossom/activation/ask-password");
        Assert.assertTrue(result.getModel().isEmpty());

        verify(userService, times(1)).getByIdentifier(eq(askPasswordForm.getLoginOrEmail()));
        verify(userService, times(1)).getByEmail(eq(askPasswordForm.getLoginOrEmail()));
    }

    @Test
    public void should_display_ask_for_forgotten_password_if_user_present_by_identifier() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ActivationController.AskPasswordForm askPasswordForm = new ActivationController.AskPasswordForm();
        askPasswordForm.setLoginOrEmail("test@test.fr");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(userService.getByIdentifier(eq(askPasswordForm.getLoginOrEmail()))).thenReturn(Optional.of(userDTO));
        when(userService.getByEmail(eq(askPasswordForm.getLoginOrEmail()))).thenReturn(Optional.empty());

        ModelAndView result = controller.askForForgottenPassword(askPasswordForm, bindingResult, new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getViewName(), "blossom/activation/ask-password");
        Assert.assertTrue((Boolean) result.getModel().get("resetPasswordMail"));

        verify(userService, times(1)).getByIdentifier(eq(askPasswordForm.getLoginOrEmail()));
        verify(userService, times(1)).getByEmail(eq(askPasswordForm.getLoginOrEmail()));
        verify(userService, times(1)).askPasswordChange(eq(userDTO.getId()));
    }

    @Test
    public void should_display_ask_for_forgotten_password_if_user_present_by_email() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ActivationController.AskPasswordForm askPasswordForm = new ActivationController.AskPasswordForm();
        askPasswordForm.setLoginOrEmail("test@test.fr");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(userService.getByIdentifier(eq(askPasswordForm.getLoginOrEmail()))).thenReturn(Optional.empty());
        when(userService.getByEmail(eq(askPasswordForm.getLoginOrEmail()))).thenReturn(Optional.of(userDTO));

        ModelAndView result = controller.askForForgottenPassword(askPasswordForm, bindingResult, new ExtendedModelMap());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getViewName(), "blossom/activation/ask-password");
        Assert.assertTrue((Boolean) result.getModel().get("resetPasswordMail"));

        verify(userService, times(1)).getByIdentifier(eq(askPasswordForm.getLoginOrEmail()));
        verify(userService, times(1)).getByEmail(eq(askPasswordForm.getLoginOrEmail()));
        verify(userService, times(1)).askPasswordChange(eq(userDTO.getId()));
    }


}
