package fr.blossom.ui.web.system.sessions;

import fr.blossom.ui.current_user.CurrentUser;
import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.security.LoginAttemptsService;
import fr.blossom.ui.stereotype.BlossomController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@BlossomController
@RequestMapping("/system/sessions")
@OpenedMenu("sessions")
@PreAuthorize("hasAuthority('system:sessions:manager')")
public class SessionController {

  private final SessionRegistry sessionRegistry;
  private final LoginAttemptsService loginAttemptsService;

  public SessionController(SessionRegistry sessionRegistry,
    LoginAttemptsService loginAttemptsService) {
    this.sessionRegistry = sessionRegistry;
    this.loginAttemptsService = loginAttemptsService;
  }

  @GetMapping
  public ModelAndView sessions(Model model) {
    Map<CurrentUser, List<SessionInformation>> sessions =
      sessionRegistry.getAllPrincipals().stream().collect(
        Collectors.toMap(p -> (CurrentUser) p, p -> sessionRegistry.getAllSessions(p, false)));

    model.addAttribute("sessions", sessions);
    model.addAttribute("attempts", loginAttemptsService.get());

    return new ModelAndView("blossom/system/sessions/sessions", model.asMap());
  }

  @PostMapping("/{sessionId}/_invalidate")
  @ResponseBody
  public void sessions(@PathVariable("sessionId") String sessionId) {
    SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
    if (sessionInformation != null) {
      sessionInformation.expireNow();
    }
  }
}
