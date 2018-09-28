package com.blossomproject.model.azureactivedirectory;

import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.blossomproject.ui.current_user.CurrentUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

public class AzureADLoginAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
    private UserDetailsService currentUserDetailsService;

    public AzureADLoginAuthenticationProvider(UserService userService, UserDetailsService currentUserDetailsService) {
        this.userService = userService;
        this.currentUserDetailsService = currentUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!supports(authentication.getClass())){
            return null;
        }
        Optional<UserDTO> userDTO = userService.getByEmail((String) ((PreAuthenticationAzureToken)authentication).getPrincipal());
        if(!userDTO.isPresent()){
            throw new UsernameNotFoundException("Microsoft user unauthorized to use this application");
        }
        CurrentUser currentUser = (CurrentUser) currentUserDetailsService.loadUserByUsername(userDTO.get().getIdentifier());
        userService.updateLastConnection(currentUser.getUser().getId(), new Date(System.currentTimeMillis()));

        return new PostAuthenticationAzureToken(Collections.emptyList(),currentUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticationAzureToken.class.isAssignableFrom(authentication);
    }
}
