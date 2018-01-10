package fr.blossom.ui.current_user;

import com.google.common.collect.Lists;
import fr.blossom.core.user.UserDTO;
import java.util.List;

public class CurrentUserBuilder {

  private UserDTO user;
  private String identifier;
  private String passwordHash;
  private List<String> privileges = Lists.newArrayList();

  public CurrentUserBuilder user(UserDTO user) {
    this.user = user;
    return this;
  }

  public CurrentUserBuilder identifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

  public CurrentUserBuilder passwordHash(String passwordHash) {
    this.passwordHash = passwordHash;
    return this;
  }

  public CurrentUserBuilder addPrivilege(String privilege) {
    this.privileges.add(privilege);
    return this;
  }

  public CurrentUser toCurrentUser() {

    if (user == null) {
      user = new UserDTO();
      user.setIdentifier(identifier);
      user.setPasswordHash(passwordHash);
      user.setActivated(true);
    }

    CurrentUser currentUser = null;
    if (privileges.isEmpty()) {
      currentUser = new CurrentUser(user);
    } else {
      currentUser = new CurrentUser(user, privileges.toArray(new String[privileges.size()]));
    }

    return currentUser;

  }
}
