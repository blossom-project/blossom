package fr.blossom.sample.builder;

import fr.blossom.core.user.User;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by zoula_000 on 19/05/2017.
 */
public class UserBuilder {

    private long generatedId = UUID.randomUUID().getLeastSignificantBits();

    private long id = generatedId;
    private String identifier = "IDENTIFIER_" + generatedId;
    private String email = "EMAIL " + generatedId;
    private String description = "DESCRIPTION " + generatedId;
    private String passwordHash = "$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C";
    private String lastname = "LASTNAME " + generatedId;
    private String firstname = "FIRSTNAME " + generatedId;
    private boolean activated = true;
    private User.Civility civility = User.Civility.MAN;
    private String company = "COMPANY " + generatedId;
    private String function = "FUNCTION " + generatedId;
    private Date lastConnection = new Date(System.currentTimeMillis());
    private String phone = "PHONE " + generatedId;
    private Locale locale = Locale.ENGLISH;

    public UserBuilder id(long id) {
        this.id = id;
        return this;
    }

    public UserBuilder identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder civility(User.Civility civility) {
        this.civility = civility;
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

    public UserBuilder locale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public User toUser() {
        User user = new User();

        user.setId(this.id);
        user.setIdentifier(this.identifier);
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
        user.setLocale(locale);

        return user;
    }

}
