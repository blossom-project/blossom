package fr.mgargadennec.blossom.ui.current_user;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import fr.mgargadennec.blossom.core.user.UserDTO;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SystemUserDetailsServiceImpl implements UserDetailsService {

  private final static String DEFAULT_IDENTIFIER = "system";
  private final static String DEFAULT_PASSWORD = "system";

  private final String identifier;
  private final String password;

  public SystemUserDetailsServiceImpl() {
    this.identifier = DEFAULT_IDENTIFIER;
    this.password = DEFAULT_PASSWORD;
  }

  public SystemUserDetailsServiceImpl(String identifier, String password) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(identifier));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(password));
    this.identifier = identifier;
    this.password = password;
  }


  @Override
  public CurrentUser loadUserByUsername(String identifier) throws UsernameNotFoundException {
    if (this.identifier.equals(identifier)) {
      UserDTO user = new UserDTO();
      user.setId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
      user.setActivated(true);
      user.setFirstname(this.identifier.toLowerCase());
      user.setLastname(this.identifier.toUpperCase());
      user.setIdentifier(this.identifier);
      user.setPasswordHash(this.password);
      user.setFunction("System");
      user.setPhone("");
      return new CurrentUser(user, "ACTUATOR");
    }

    throw new UsernameNotFoundException(
      String.format("User with email=%s was not found", identifier));
  }

}
