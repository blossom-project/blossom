package fr.mgargadennec.blossom.ui.web.system.sessions;

import fr.mgargadennec.blossom.ui.current_user.CurrentUser;
import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@BlossomController("/system/sessions")
@OpenedMenu("sessions")
public class SessionController {

  private final SessionRegistry sessionRegistry;

  public SessionController(SessionRegistry sessionRegistry) {
    this.sessionRegistry = sessionRegistry;
  }

  @GetMapping
  public ModelAndView sessions() {
    Map<CurrentUser, List<SessionInformation>> sessions =
      sessionRegistry.getAllPrincipals().stream().collect(
        Collectors.toMap(p -> (CurrentUser) p, p -> sessionRegistry.getAllSessions(p, false)));

    return new ModelAndView("system/sessions/sessions", "sessions", sessions);
  }
}
