package fr.mgargadennec.blossom.ui.current_user;

import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CurrentUserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;

  public CurrentUserDetailsServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public CurrentUser loadUserByUsername(String identifier) throws UsernameNotFoundException {
    UserDTO user = userService.getByIdentifier(identifier).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found", identifier)));

    return new CurrentUser(user);
  }

}
