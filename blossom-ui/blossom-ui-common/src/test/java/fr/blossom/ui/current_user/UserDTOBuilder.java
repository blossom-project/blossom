package fr.blossom.ui.current_user;

import fr.blossom.core.user.UserDTO;

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
