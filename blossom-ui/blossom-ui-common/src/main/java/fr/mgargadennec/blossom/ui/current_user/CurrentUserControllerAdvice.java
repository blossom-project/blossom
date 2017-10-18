package fr.mgargadennec.blossom.ui.current_user;

import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = BlossomController.class)
public class CurrentUserControllerAdvice {

  @ModelAttribute("currentUser")
  public CurrentUser getCurrentUser(Authentication authentication) {
    return (authentication == null) ? null : (CurrentUser) authentication.getPrincipal();
  }

}
