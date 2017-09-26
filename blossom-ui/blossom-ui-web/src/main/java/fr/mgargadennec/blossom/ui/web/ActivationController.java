package fr.mgargadennec.blossom.ui.web;

import fr.mgargadennec.blossom.core.common.utils.action_token.ActionToken;
import fr.mgargadennec.blossom.core.common.utils.action_token.ActionTokenService;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.core.validation.FieldMatch;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
      return "redirect:/blossom";
    }
    if (actionToken.isValid() && actionToken.getAction().equals(UserService.USER_ACTIVATION)) {
      Optional<UserDTO> user = this.userService.getById(actionToken.getUserId());
      if (user.isPresent()) {
        Long userId = user.get().getId();
        this.userService.updateActivation(userId, true);

        ActionToken passwordResetToken = new ActionToken();
        passwordResetToken.setAction(UserService.USER_RESET_PASSWORD);
        passwordResetToken.setExpirationDate(LocalDateTime.now().plusMinutes(30));
        passwordResetToken.setUserId(userId);

        return "redirect:/blossom/public/change_password?token=" + this.tokenService.generateToken(passwordResetToken);
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
      Optional<UserDTO> user = this.userService.getById(actionToken.getUserId());

      if (user.isPresent()) {
        UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();
        updatePasswordForm.setToken(token);

        return new ModelAndView("activation/change-password", "updatePasswordForm", updatePasswordForm);
      }
    }
    return new ModelAndView(new RedirectView("/blossom"));
  }

  @PostMapping("/change_password")
  public ModelAndView changePassword(Model model, @Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm updatePasswordForm, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ModelAndView("activation/change-password", "updatePasswordForm", updatePasswordForm);
    }

    ActionToken actionToken;
    try {
      actionToken = this.tokenService.decryptToken(updatePasswordForm.getToken());
    } catch (Exception e) {
      return new ModelAndView(new RedirectView("/blossom"));
    }

    if (actionToken.isValid() && actionToken.getAction().equals(UserService.USER_RESET_PASSWORD)) {
      Optional<UserDTO> user = this.userService.getById(actionToken.getUserId());

      if (user.isPresent()) {
        this.userService.updatePassword(user.get().getId(), updatePasswordForm.getPassword());
      }
    }
    return new ModelAndView(new RedirectView("/blossom"));
  }

  @GetMapping("/forgotten_password")
  public ModelAndView askForForgottenPassword() {
    return new ModelAndView("activation/ask-password", "askPasswordForm", new AskPasswordForm());
  }

  @PostMapping("/forgotten_password")
  public ModelAndView askForForgottenPassword(
    @Valid @ModelAttribute("askPasswordForm") AskPasswordForm form, BindingResult bindingResult, Model model)
    throws Exception {
    LOGGER.info("Demande de réinitialisation du mot de passe de l'utilisateur " + form.getLoginOrEmail());
    if (!bindingResult.hasErrors()) {
      Optional<UserDTO> userDTO = this.userService.getByIdentifier(form.getLoginOrEmail());
      if (userDTO.isPresent()) {
        this.userService.askPasswordChange(userDTO.get().getId());
      } else {
        userDTO = this.userService.getByEmail(form.getLoginOrEmail());
        if (userDTO.isPresent()) {
          this.userService.askPasswordChange(userDTO.get().getId());
        }
      }
      model.addAttribute("resetPasswordMail", true);
    }

    return new ModelAndView("activation/ask-password", model.asMap());
  }

  @FieldMatch(message = "{change.password.validation.FieldMatch.message}", value = "password", confirmation = "passwordRepeater")
  public static class UpdatePasswordForm {
    @NotEmpty
    private String token;

    @NotEmpty(message="{change.password.validation.NotEmpty.message}")
    @Size(min=8, message="{change.password.validation.Size.message}")
    @Pattern.List({
      @Pattern(regexp = "(?=.*[0-9]).+", message = "{change.password.validation.Pattern.digit.message}"),
      @Pattern(regexp = "(?=.*[a-z]).+", message = "{change.password.validation.Pattern.lowercase.message}"),
      @Pattern(regexp = "(?=.*[A-Z]).+", message = "{change.password.validation.Pattern.uppercase.message}")
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
    @NotEmpty(message="{ask.password.validation.NotEmpty.message}")
    private String loginOrEmail ="";

    public String getLoginOrEmail() {
      return loginOrEmail;
    }

    public void setLoginOrEmail(String loginOrEmail) {
      this.loginOrEmail = loginOrEmail;
    }
  }
}
