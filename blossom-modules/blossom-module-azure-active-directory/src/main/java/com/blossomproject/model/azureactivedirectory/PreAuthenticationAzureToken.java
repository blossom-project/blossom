package com.blossomproject.model.azureactivedirectory;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class PreAuthenticationAzureToken extends AbstractAuthenticationToken {
    private String email;

    public PreAuthenticationAzureToken(String email) {
        super(Collections.emptyList());
        this.email = email;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }
}
