package com.blossomproject.ui.current_user;

import com.blossomproject.core.user.UserDTO;
import java.util.Set;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

  private static final long serialVersionUID = 1L;
  private UserDTO user;

  public CurrentUser(UserDTO user) {
    super(user.getIdentifier(), user.getPasswordHash(), AuthorityUtils.createAuthorityList());
    this.user = user;
  }

  public CurrentUser(UserDTO user, String... privileges) {
    super(user.getIdentifier(), user.getPasswordHash(),
      AuthorityUtils.createAuthorityList(privileges));
    this.user = user;
  }

  public CurrentUser(UserDTO user, Set<String> privileges) {
    super(user.getIdentifier(), user.getPasswordHash(),
      AuthorityUtils.createAuthorityList(privileges.toArray(new String[privileges.size()])));
    this.user = user;
  }

  public UserDTO getUser() {
    return this.user;
  }

  @Override
  public boolean isEnabled() {
    return this.user.isActivated();
  }

  public boolean hasPrivilege(String privilege) {
    return this.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(privilege));
  }

  @Override
  public String toString() {
    return "CurrentUser{" + "user=" + this.user + ", privileges=" + this.getAuthorities() + "} "
      + super.toString();
  }
}
