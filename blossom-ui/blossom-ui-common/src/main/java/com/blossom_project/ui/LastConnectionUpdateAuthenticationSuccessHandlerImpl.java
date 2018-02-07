package com.blossom_project.ui;

import com.blossom_project.core.user.UserService;
import com.blossom_project.ui.current_user.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class LastConnectionUpdateAuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {

  private final UserService userService;

  public LastConnectionUpdateAuthenticationSuccessHandlerImpl(UserService userService) {
    this.userService = userService;
    this.setDefaultTargetUrl("/blossom");
    this.setAlwaysUseDefaultTargetUrl(false);
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    throws IOException, ServletException {

    CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
    userService.updateLastConnection(currentUser.getUser().getId(), new Date(System.currentTimeMillis()));

    super.onAuthenticationSuccess(request, response, authentication);
  }
}
