package fr.mgargadennec.blossom.ui.current_user;

import fr.mgargadennec.blossom.core.user.UserDTO;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {
  private static final long serialVersionUID = 1L;

  private UserDTO user;

  public CurrentUser(UserDTO user) {
    super(user.getIdentifier(), user.getPasswordHash(), AuthorityUtils.createAuthorityList());
    this.user = user;
  }

  public CurrentUser(UserDTO user, String... roles) {
    super(user.getIdentifier(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(roles));
    this.user = user;
  }

  public UserDTO getUser() {
    return this.user;
  }

  @Override
  public boolean isEnabled() {
    return this.user.isActivated();
  }

  @Override
  public String toString() {
    return "CurrentUser{" + "user=" + this.user + "} " + super.toString();
  }
}
