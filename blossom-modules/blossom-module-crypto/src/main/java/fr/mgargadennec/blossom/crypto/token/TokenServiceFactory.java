package fr.mgargadennec.blossom.crypto.token;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenServiceFactory {

    private final SecureRandom random;

    public TokenServiceFactory(SecureRandom random) {
        this.random = random;
    }

    public String generateSecret() {
        byte[] secretBytes = new byte[32];
        random.nextBytes(secretBytes);
        return Base64.getEncoder().encodeToString(secretBytes);
    }

    /**
     * Create a TokenFactory that keeps data encrypted using a secret.
     * Multiple instances using the same secret will be able to decrypt tokens produced by others.
     *
     * @param secret The secret used to crypt/decrypt data.
     * @return A TokenService instance.
     */
    public TokenService secretTokenService(String secret) {
        return new SecretTokenService(secret);
    }
}
