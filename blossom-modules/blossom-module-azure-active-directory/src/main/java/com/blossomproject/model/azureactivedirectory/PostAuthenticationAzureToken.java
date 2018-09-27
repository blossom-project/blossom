package com.blossomproject.model.azureactivedirectory;

import com.blossomproject.ui.current_user.CurrentUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PostAuthenticationAzureToken extends AbstractAuthenticationToken {
    private CurrentUser currentUser;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     * @param currentUser
     */
    public PostAuthenticationAzureToken(Collection<? extends GrantedAuthority> authorities, CurrentUser currentUser) {
        super(authorities);
        this.currentUser = currentUser;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return currentUser;
    }


}
