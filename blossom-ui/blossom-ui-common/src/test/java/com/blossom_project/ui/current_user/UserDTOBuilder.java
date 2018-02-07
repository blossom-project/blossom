package com.blossom_project.ui.current_user;

import com.blossom_project.core.user.UserDTO;

public class UserDTOBuilder {

    private String identifier;
    private String passwordHash;

    public UserDTOBuilder identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public UserDTOBuilder passwordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public UserDTO toUserDTO() {
        UserDTO user = new UserDTO();

        user.setIdentifier(identifier);
        user.setPasswordHash(passwordHash);

        return user;
    }

}
