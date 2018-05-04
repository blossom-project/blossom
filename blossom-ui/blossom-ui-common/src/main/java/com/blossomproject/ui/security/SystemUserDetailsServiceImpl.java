package com.blossomproject.ui.security;

import com.blossomproject.core.user.User.Civility;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.ui.current_user.CurrentUser;
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
      user.setFunction("Administrator");
      user.setCompany("");
      user.setPhone("");
      user.setEmail("");
      user.setCivility(Civility.UNKNOWN);

      Set<String> privileges = privilegeRegistry.getPlugins().stream()
        .map(Privilege::privilege).collect(
          Collectors.toSet());

      CurrentUser currentUser = new CurrentUser(user, privileges);
      currentUser.setUpdatable(false);
      return currentUser;
    }

    throw new UsernameNotFoundException(
      String.format("User with email=%s was not found", identifier));
  }

}
