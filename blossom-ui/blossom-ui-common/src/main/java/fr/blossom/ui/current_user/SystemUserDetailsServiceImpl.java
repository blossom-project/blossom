package fr.blossom.ui.current_user;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.user.UserDTO;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SystemUserDetailsServiceImpl implements UserDetailsService {

  private final static String DEFAULT_IDENTIFIER = "system";
  private final static String DEFAULT_PASSWORD = "system";

  private final PluginRegistry<Privilege, String> privilegeRegistry;
  private final String identifier;
  private final String password;

  public SystemUserDetailsServiceImpl(
    PluginRegistry<Privilege, String> privilegeRegistry) {
    this.privilegeRegistry = privilegeRegistry;
    this.identifier = DEFAULT_IDENTIFIER;
    this.password = DEFAULT_PASSWORD;
  }

  public SystemUserDetailsServiceImpl(PluginRegistry<Privilege, String> privilegeRegistry,
    String identifier, String password) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(identifier));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(password));
    this.privilegeRegistry = privilegeRegistry;
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

      Set<String> privileges = privilegeRegistry.getPlugins().stream()
        .map(plugin -> plugin.privilege()).collect(
          Collectors.toSet());

      return new CurrentUser(user, privileges);
    }

    throw new UsernameNotFoundException(
      String.format("User with email=%s was not found", identifier));
  }

}
