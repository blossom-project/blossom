package fr.blossom.ui.current_user;

import fr.blossom.core.user.UserDTO;

public class CurrentUserBuilder {

    private UserDTO user;
    private String identifier;
    private String passwordHash;

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

    public CurrentUser toCurrentUser() {

        if (user == null) {
            user = new UserDTO();
            user.setIdentifier(identifier);
            user.setPasswordHash(passwordHash);
            user.setActivated(true);
        }

        CurrentUser currentUser = new CurrentUser(user);

        return currentUser;

    }
}
