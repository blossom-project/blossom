package fr.mgargadennec.blossom.ui.current_user;

import fr.mgargadennec.blossom.core.user.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

public class SystemUserDetailsServiceImpl implements UserDetailsService {

  @Override
  public CurrentUser loadUserByUsername(String identifier) throws UsernameNotFoundException {
    if ("system".equals(identifier)) {
      UserDTO user = new UserDTO();
      user.setId(UUID.randomUUID().getLeastSignificantBits());
      user.setActivated(true);
      user.setFirstname("System");
      user.setLastname("Root");
      user.setIdentifier("system");
      user.setPasswordHash("system");
      user.setFunction("System");
      user.setPhone("");
      return new CurrentUser(user);
    }

    throw new UsernameNotFoundException(String.format("User with email=%s was not found", identifier));
  }

}
