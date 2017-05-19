package fr.mgargadennec.blossom.integration.builder;

import fr.mgargadennec.blossom.core.user.User;

import java.util.Date;
import java.util.UUID;

/**
 * Created by zoula_000 on 19/05/2017.
 */
public class UserBuilder {

    private long id = UUID.randomUUID().getLeastSignificantBits();
    private String email = "EMAIL " + UUID.randomUUID().getLeastSignificantBits();
    private String description = "DESCRIPTION " + UUID.randomUUID().getLeastSignificantBits();
    private String passwordHash = "$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C";
    private String lastname = "LASTNAME " + UUID.randomUUID().getLeastSignificantBits();
    private String firstname = "FIRSTNAME " + UUID.randomUUID().getLeastSignificantBits();
    private boolean activated = true;
    private User.Civility civility = User.Civility.MAN;
    private String company = "COMPANY " + UUID.randomUUID().getLeastSignificantBits();
    private String function = "FUNCTION " + UUID.randomUUID().getLeastSignificantBits();
    private Date lastConnection = new Date(System.currentTimeMillis());
    private String phone = "PHONE " + UUID.randomUUID().getLeastSignificantBits();

    public UserBuilder id(long id) {
        this.id = id;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public UserBuilder lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public UserBuilder passwordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public UserBuilder activated(boolean activated) {
        this.activated = activated;
        return this;
    }

    public User toUser() {
        User user = new User();

        user.setId(this.id);
        user.setEmail(this.email);
        user.setDescription(this.description);
        user.setPasswordHash(this.passwordHash);
        user.setLastname(this.lastname);
        user.setFirstname(this.firstname);
        user.setActivated(this.activated);
        user.setCivility(this.civility);
        user.setCompany(this.company);
        user.setFunction(this.function);
        user.setLastConnection(this.lastConnection);
        user.setPhone(this.phone);

        return user;
    }

}
