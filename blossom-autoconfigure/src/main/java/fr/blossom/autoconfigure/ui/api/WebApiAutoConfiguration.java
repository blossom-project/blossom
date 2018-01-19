package fr.blossom.autoconfigure.ui.api;

import static fr.blossom.autoconfigure.ui.WebContextAutoConfiguration.BLOSSOM_API_BASE_PATH;

import fr.blossom.autoconfigure.ui.WebSecurityAutoConfiguration;
import fr.blossom.core.association_user_group.AssociationUserGroupService;
import fr.blossom.core.association_user_role.AssociationUserRoleService;
import fr.blossom.core.common.PluginConstants;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.search.SearchEngine;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.group.GroupDTO;
import fr.blossom.core.group.GroupService;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleService;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.api.OmnisearchApiController;
import fr.blossom.ui.api.StatusApiController;
import fr.blossom.ui.api.administration.GroupsApiController;
import fr.blossom.ui.api.administration.MembershipsApiController;
import fr.blossom.ui.api.administration.ResponsabilitiesApiController;
import fr.blossom.ui.api.administration.RolesApiController;
import fr.blossom.ui.api.administration.UsersApiController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.tika.Tika;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity.RequestMatcherConfigurer;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(UsersApiController.class)
public class WebApiAutoConfiguration {

  @Bean
  public UsersApiController usersApiController(UserService userService,
    SearchEngineImpl<UserDTO> searchEngine, Tika tika) {
    return new UsersApiController(userService, searchEngine, tika);
  }

  @Bean
  public GroupsApiController groupsApiController(GroupService groupService,
    SearchEngineImpl<GroupDTO> searchEngine) {
    return new GroupsApiController(groupService, searchEngine);
  }

  @Bean
  public RolesApiController rolesApiController(RoleService roleService,
    SearchEngineImpl<RoleDTO> searchEngine) {
    return new RolesApiController(roleService, searchEngine);
  }

  @Bean
  public MembershipsApiController membershipsApiController(
    AssociationUserGroupService associationUserGroupService, UserService userService,
    GroupService groupService) {
    return new MembershipsApiController(associationUserGroupService, userService, groupService);
  }

  @Bean
  public ResponsabilitiesApiController responsabilitiesApiController(
    AssociationUserRoleService associationUserRoleService, UserService userService,
    RoleService roleService) {
    return new ResponsabilitiesApiController(associationUserRoleService, userService, roleService);
  }

  @Bean
  public OmnisearchApiController omnisearchApiController(Client client,
    @Qualifier(PluginConstants.PLUGIN_SEARCH_ENGINE) PluginRegistry<SearchEngine, Class<? extends AbstractDTO>> registry) {
    return new OmnisearchApiController(client, registry);
  }

  @Bean
  public StatusApiController statusApiController(HealthEndpoint healthEndpoint) {
    return new StatusApiController(healthEndpoint);
  }

  @Configuration
  @EnableResourceServer
  @AutoConfigureAfter(WebSecurityAutoConfiguration.class)
  public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired(required = false)
    private AuthorizationServerEndpointsConfiguration endpoints;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
      resources.resourceId("blossom-api").stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests().anyRequest().fullyAuthenticated();

      RequestMatcherConfigurer requests = http.requestMatchers();
      if (endpoints != null) {
        // Assume we are in an Authorization Server
        requests
          .requestMatchers(new AndRequestMatcher(
            new NotOAuthRequestMatcher(endpoints.oauth2EndpointHandlerMapping()),
            new AntPathRequestMatcher("/" + BLOSSOM_API_BASE_PATH + "/**")));
      }
    }
  }

  private static class NotOAuthRequestMatcher implements RequestMatcher {

    private Set<String> mappings;

    public NotOAuthRequestMatcher(FrameworkEndpointHandlerMapping mapping) {
      this.mappings = mapping.getHandlerMethods().keySet().stream().flatMap(k -> k.getPatternsCondition().getPatterns().stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean matches(HttpServletRequest request) {
      final String requestPath = getRequestPath(request);
      return mappings.stream().noneMatch(path -> requestPath.startsWith(path));
    }

    private String getRequestPath(HttpServletRequest request) {
      String url = request.getServletPath();

      if (request.getPathInfo() != null) {
        url += request.getPathInfo();
      }

      return url;
    }

  }

  @Configuration
  @EnableAuthorizationServer
  @AutoConfigureAfter(WebSecurityAutoConfiguration.class)
  public static class AuthorizationServerConfiguration extends
    AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      endpoints
        .pathMapping("/oauth/authorize", "/" + BLOSSOM_API_BASE_PATH + "/oauth/authorize")
        .pathMapping("/oauth/check_token", "/" + BLOSSOM_API_BASE_PATH + "/oauth/token")
        .pathMapping("/oauth/confirm_access", "/" + BLOSSOM_API_BASE_PATH + "/oauth/confirm_access")
        .pathMapping("/oauth/error", "/" + BLOSSOM_API_BASE_PATH + "/oauth/error")
        .pathMapping("/oauth/token", "/" + BLOSSOM_API_BASE_PATH + "/oauth/token")
        .tokenStore(tokenStore())
        .tokenServices(tokenServices())
        .tokenGranter(tokenGranter())
        .authenticationManager(this.authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients.inMemory()
        .withClient("blossom-client")
        .authorizedGrantTypes("password", "refresh_token", "action_token")
        .scopes("read", "write")
        .accessTokenValiditySeconds(300)
        .refreshTokenValiditySeconds(600)
        .resourceIds("blossom-api");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
      security.passwordEncoder(passwordEncoder).checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public TokenGranter tokenGranter() {
      List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
      tokenGranters.add(new RefreshTokenGranter(tokenServices(), clientDetailsService, oauth2RequestFactory()));
      tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices(), clientDetailsService, oauth2RequestFactory()));

      return new CompositeTokenGranter(tokenGranters);
    }

    @Bean
    public OAuth2RequestFactory oauth2RequestFactory() {
      return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    @Bean
    public TokenStore tokenStore() {
      return new InMemoryTokenStore();
    }


    @Bean
    @Primary
    public AuthorizationServerTokenServices tokenServices() {
      DefaultTokenServices tokenServices = new DefaultTokenServices();
      tokenServices.setSupportRefreshToken(true);
      tokenServices.setReuseRefreshToken(true);
      tokenServices.setAuthenticationManager(authenticationManager);
      tokenServices.setClientDetailsService(clientDetailsService);
      tokenServices.setTokenStore(tokenStore());

      PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
      provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
      tokenServices.setAuthenticationManager(new ProviderManager(Arrays.<AuthenticationProvider>asList(provider)));

      return tokenServices;
    }

  }

}
