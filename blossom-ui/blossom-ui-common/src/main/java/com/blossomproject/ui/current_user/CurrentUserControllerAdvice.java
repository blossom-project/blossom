package com.blossomproject.ui.current_user;

import com.blossomproject.ui.stereotype.BlossomController;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = BlossomController.class)
public class CurrentUserControllerAdvice {

  @ModelAttribute("currentUser")
  public CurrentUser getCurrentUser(Authentication authentication) {
    return (authentication == null || authentication.getPrincipal()==null || !(authentication.getPrincipal() instanceof CurrentUser)) ? null : (CurrentUser) authentication.getPrincipal();
  }

}
