package com.blossomproject.ui.web;

import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.blossomproject.core.validation.FieldMatch;
import com.blossomproject.ui.current_user.CurrentUser;
import com.blossomproject.ui.stereotype.BlossomController;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@BlossomController
@RequestMapping("/profile")
public class ProfileController {

  private final UserService userService;

  public ProfileController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ModelAndView profile() {
    return new ModelAndView("blossom/profile/profile");
  }


  @GetMapping("/password/_edit")
  public ModelAndView updatePasswordForm() {
    return new ModelAndView("blossom/profile/profilepassword-edit-modal", "updatePasswordForm",
      new UpdatePasswordForm());
  }

  @PostMapping("/password/_edit")
  public ModelAndView changePassword(Authentication authentication,
    @Valid @ModelAttribute("updatePasswordForm") UpdatePasswordForm updatePasswordForm,
    BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ModelAndView("blossom/profile/profilepassword-edit-modal", "updatePasswordForm", updatePasswordForm);
    }
    CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

    Optional<UserDTO> user = this.userService.getById(currentUser.getUser().getId());

    if (user.isPresent()) {
      this.userService.updatePassword(user.get().getId(), updatePasswordForm.getPassword());
    }

    return new ModelAndView("blossom/profile/profilepassword-edited-modal");
  }

  @FieldMatch(message = "{change.password.validation.FieldMatch.message}", value = "password", confirmation = "passwordRepeater")
  public static class UpdatePasswordForm {

    @NotEmpty(message = "{change.password.validation.NotEmpty.message}")
    @Size(min = 8, message = "{change.password.validation.Size.message}")
    @Pattern.List({
      @Pattern(regexp = "(?=.*[0-9]).+", message = "{change.password.validation.Pattern.digit.message}"),
      @Pattern(regexp = "(?=.*[a-z]).+", message = "{change.password.validation.Pattern.lowercase.message}"),
      @Pattern(regexp = "(?=.*[\\p{P}\\p{S}]).+", message = "{change.password.validation.Pattern.specialchar.message}")
    })
    private String password = "";

    private String passwordRepeater = "";


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
}
