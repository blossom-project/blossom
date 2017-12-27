package fr.blossom.ui.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CompositeUserDetailsServiceImpl implements UserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CompositeUserDetailsServiceImpl.class);

  private UserDetailsService[] userDetailsServices;

  public CompositeUserDetailsServiceImpl(UserDetailsService... userDetailsServices) {
    this.userDetailsServices = userDetailsServices;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    for (UserDetailsService userDetailsService : userDetailsServices) {
      try {
        return userDetailsService.loadUserByUsername(username);
      } catch (UsernameNotFoundException e) {
        LOGGER.debug("Could not find username {} with userDetailsService {}", username, userDetailsService.getClass());
      }
    }
    throw new UsernameNotFoundException("No configured userDetailsService was capable of providing a user with username " + username);
  }
}
