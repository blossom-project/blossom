package fr.blossom.crypto.token;

import org.springframework.security.core.token.TokenService;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Factory to quickly create TokenService instances of different kinds.
 * An instance is provided by Blossom's autoconfiguration.
 */
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
   * Create a TokenService that keeps data encrypted using a secret.
   * Tokens are stateless:
   * - multiple instances using the same secret will be able to decrypt tokens produced by others,
   * even across different applications/servers
   * - features such as token revocation, counting etc... Must be added on top.
   *
   * @param secret The secret used to crypt/decrypt data.
   * @return A TokenService instance.
   */
  public TokenService statelessSecretTokenService(String secret) {
    return new StatelessSecretTokenService(secret);
  }
}
