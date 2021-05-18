package com.blossomproject.ui.web;

import com.blossomproject.core.common.utils.action_token.ActionToken;
import com.blossomproject.core.common.utils.action_token.ActionTokenService;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.blossomproject.core.validation.FieldMatch;
import com.blossomproject.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

@BlossomController
@RequestMapping("/public")
public class ActivationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ActivationController.class);

  private final ActionTokenService tokenService;
  private final UserService userService;

  @Autowired
  public ActivationController(ActionTokenService tokenService, UserService userService) {
    this.tokenService = tokenService;
    this.userService = userService;
  }

  @GetMapping("/activate")
  public String activateAccount(@RequestParam("token") String token, Model model) {
    ActionToken actionToken;
    try {
      actionToken = this.tokenService.decryptToken(token);
    } catch (Exception e) {
      LOGGER.error("Cannot decrypt action token", e);
      return "redirect:/blossom";
    }
    if (actionToken.isValid() && actionToken.getAction().equals(UserService.USER_ACTIVATION)) {
      Optional<UserDTO> user = this.userService.getByActionToken(actionToken);
      if (user.isPresent()) {
        Long userId = user.get().getId();
        this.userService.updateActivation(userId, true);

        return "redirect:/blossom/public/change_password?token=" + userService
          .generatePasswordResetToken(user.get());
      }
    }
    return "redirect:/blossom";
  }

  @GetMapping("/change_password")
  public ModelAndView resetPassword(@RequestParam("token") String token, Model model) {
    ActionToken actionToken;
    try {
      actionToken = this.tokenService.decryptToken(token);
    } catch (Exception e) {
      return new ModelAndView(new RedirectView("/blossom"));
    }
    if (actionToken.isValid() && actionToken.getAction().equals(UserService.USER_RESET_PASSWORD)) {
      Optional<UserDTO> user = this.userService.getByActionToken(actionToken);

      if (user.isPresent()) {
        UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();
        updatePasswordForm.setToken(token);

        return new ModelAndView(
          "blossom/activation/change-password",
          "updatePasswordForm",
          updatePasswordForm);
      }
    }
    return new ModelAndView(new RedirectView("/blossom"));
  }

  @PostMapping("/change_password")
  public ModelAndView changePassword(Model model,
                                     @Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm updatePasswordForm,
                                     BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ModelAndView("blossom/activation/change-password", "updatePasswordForm",
        updatePasswordForm);
    }

    ActionToken actionToken;
    try {
      actionToken = this.tokenService.decryptToken(updatePasswordForm.getToken());
    } catch (Exception e) {
      return new ModelAndView(new RedirectView("/blossom"));
    }

    if (actionToken.isValid() && actionToken.getAction().equals(UserService.USER_RESET_PASSWORD)) {
      Optional<UserDTO> user = this.userService.getByActionToken(actionToken);

      if (user.isPresent()) {
        this.userService.updatePassword(user.get().getId(), updatePasswordForm.getPassword());
      }
    }
    return new ModelAndView(new RedirectView("/blossom"));
  }

  @GetMapping("/forgotten_password")
  public ModelAndView askForForgottenPassword() {
    return new ModelAndView("blossom/activation/ask-password", "askPasswordForm", new AskPasswordForm());
  }

  @PostMapping("/forgotten_password")
  public ModelAndView askForForgottenPassword(
    @Valid @ModelAttribute("askPasswordForm") AskPasswordForm form, BindingResult bindingResult,
    Model model)
    throws Exception {

    LOGGER.info("Demande de réinitialisation du mot de passe de l'utilisateur " + form.getLoginOrEmail());
    if (!bindingResult.hasErrors()) {
      UserDTO userDTO = this.userService.getByIdentifier(form.getLoginOrEmail())
        .orElse(this.userService.getByEmail(form.getLoginOrEmail()).orElse(null));
      if (userDTO != null) {
        this.userService.askPasswordChange(userDTO.getId());
        model.addAttribute("resetPasswordMail", true);
      }
    }

    return new ModelAndView("blossom/activation/ask-password", model.asMap());
  }

  @FieldMatch(message = "{change.password.validation.FieldMatch.message}", value = "password", confirmation = "passwordRepeater")
  public static class UpdatePasswordForm {

    @NotEmpty
    private String token;

    @NotEmpty(message = "{change.password.validation.NotEmpty.message}")
    @Size(min = 8, message = "{change.password.validation.Size.message}")
    @Pattern.List({
      @Pattern(regexp = "(?=.*[0-9]).+", message = "{change.password.validation.Pattern.digit.message}"),
      @Pattern(regexp = "(?=.*[a-z]).+", message = "{change.password.validation.Pattern.lowercase.message}"),
      @Pattern(regexp = "(?=.*[\\p{P}\\p{S}]).+", message = "{change.password.validation.Pattern.specialchar.message}")
    })
    private String password = "";

    private String passwordRepeater = "";

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getPasswordRepeater() {
      return passwordRepeater;
    }

    public void setPasswordRepeater(String passwordRepeater) {
      this.passwordRepeater = passwordRepeater;
    }
  }

  public static class AskPasswordForm {

    @NotEmpty(message = "{ask.password.validation.NotEmpty.message}")
    private String loginOrEmail = "";

    public String getLoginOrEmail() {
      return loginOrEmail;
    }

    public void setLoginOrEmail(String loginOrEmail) {
      this.loginOrEmail = loginOrEmail;
    }
  }
}
