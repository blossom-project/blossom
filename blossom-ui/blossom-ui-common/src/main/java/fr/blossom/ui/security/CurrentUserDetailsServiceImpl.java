package fr.blossom.ui.security;

import fr.blossom.core.association_user_role.AssociationUserRoleDTO;
import fr.blossom.core.association_user_role.AssociationUserRoleService;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.current_user.CurrentUser;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CurrentUserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;
  private final AssociationUserRoleService associationUserRoleService;

  public CurrentUserDetailsServiceImpl(UserService userService,
    AssociationUserRoleService associationUserRoleService) {
    this.userService = userService;
    this.associationUserRoleService = associationUserRoleService;
  }

  @Override
  public CurrentUser loadUserByUsername(String identifier) throws UsernameNotFoundException {
    UserDTO user = userService.getByIdentifier(identifier).orElseThrow(
      () -> new UsernameNotFoundException(
        String.format("User with email=%s was not found", identifier)));

    List<AssociationUserRoleDTO> associationsUserRoles = associationUserRoleService
      .getAllLeft(user);

    Set<String> mergedPrivileges = associationsUserRoles.stream()
      .flatMap(association -> association.getB().getPrivileges().stream()).collect(
        Collectors.toSet());

    return new CurrentUser(user, mergedPrivileges);
  }

}
