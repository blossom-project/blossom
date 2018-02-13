package com.blossomproject.ui.security;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.blossomproject.core.association_user_role.AssociationUserRoleService;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import com.blossomproject.ui.current_user.CurrentUser;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class SystemUserDetailsServiceImplTest {

  private List<Privilege> privileges;
  private PluginRegistry<Privilege, String> privilegeRegistry;
  private AssociationUserRoleService associationUserRoleService;
  private SystemUserDetailsServiceImpl systemUserDetailsService;

  @Before
  public void setUp() throws Exception {
    this.privileges = Lists.newArrayList(new SimplePrivilege("test:test:test"), new SimplePrivilege("test:test:test2"));
    this.privilegeRegistry = mock(PluginRegistry.class);
    when(this.privilegeRegistry.getPlugins()).thenReturn(this.privileges);
    this.systemUserDetailsService = new SystemUserDetailsServiceImpl(this.privilegeRegistry);
  }

  @Test(expected = UsernameNotFoundException.class)
  public void loadSystemUserByUsername_should_throw_exception_when_no_user_found() {
    this.systemUserDetailsService.loadUserByUsername("anyString");
  }

  @Test
  public void loadSystemUserUsername_should_return_default_user_with_all_privileges() {
    CurrentUser currentUser = this.systemUserDetailsService.loadUserByUsername("system");
    assertTrue(currentUser.getUsername().equals("system"));
    assertTrue(this.privilegeRegistry.getPlugins().stream().map(Privilege::privilege).collect(
      Collectors.toList()).containsAll(
      currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList())));
  }

  @Test
  public void loadSystemUserUsername_should_return_custom_user() {
    String login = "LOGIN", password = "PASSWORD";

    this.systemUserDetailsService = new SystemUserDetailsServiceImpl(this.privilegeRegistry, login,
      password);
    CurrentUser currentUser = this.systemUserDetailsService.loadUserByUsername(login);
    assertTrue(currentUser.getUsername().equals(login));
  }

}
