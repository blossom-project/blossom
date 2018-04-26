package com.blossomproject.ui.current_user;

import com.blossomproject.ui.stereotype.BlossomController;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = BlossomController.class)
public class CurrentUserControllerAdvice {

  @ModelAttribute("currentUser")
  public CurrentUser getCurrentUser(Authentication authentication) {
    return (authentication == null || authentication.getPrincipal()==null || !(authentication.getPrincipal() instanceof CurrentUser)) ? null : (CurrentUser) authentication.getPrincipal();
  }

  @ModelAttribute("impersonating")
  public boolean isImpersonating(Authentication authentication) {
    return (authentication == null || authentication.getPrincipal()==null || !(authentication.getPrincipal() instanceof CurrentUser)) ? false : authentication.getAuthorities().stream().anyMatch(a -> a instanceof SwitchUserGrantedAuthority);
  }

  @ModelAttribute("originalUser")
  public CurrentUser getOriginalUser(Authentication authentication) {
    return isImpersonating(authentication) ? (CurrentUser) authentication.getAuthorities()
      .stream()
      .filter(a -> a instanceof SwitchUserGrantedAuthority)
      .map(a -> ((SwitchUserGrantedAuthority)a).getSource().getPrincipal())
      .findAny().orElse(getCurrentUser(authentication)) : getCurrentUser(authentication);
  }

}
