package fr.blossom.core.crypto.token;

import com.google.common.base.Preconditions;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import org.springframework.security.core.token.DefaultToken;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.encrypt.BouncyCastleAesGcmBytesEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 * An implementation of {@link TokenService} that generates an AES 256 crypted token with a secret.
 *
 * @author Raphaël Lejolivet
 *
 */
public class StatelessSecretTokenService implements TokenService {

  private final String secret;

  public StatelessSecretTokenService(String secret) {
    Preconditions.checkArgument(secret != null, "Provided secret can't be null");
    this.secret = secret;
  }

  @Override
  public Token allocateToken(String extendedInformation) {
    Date createdAt = new Date();
    String fullInformation = Long.toString(createdAt.getTime()) + ":" + extendedInformation;
    String key = cryptBytes(fullInformation.getBytes(StandardCharsets.UTF_8));
    return new DefaultToken(key, createdAt.getTime(), extendedInformation);
  }

  @Override
  public Token verifyToken(String key) {
    String fullInformation = new String(decryptBytes(key));
    String[] informationParts = fullInformation.split(":");

    Date createdAt = new Date(Long.parseLong(informationParts[0]));
    String extendedInformation = String
      .join(":", Arrays.copyOfRange(informationParts, 1, informationParts.length));

    return new DefaultToken(key, createdAt.getTime(), extendedInformation);
  }

  /**
   * Crypt a (non-null) byte array into a token
   *
   * @param data A byte array to turn into a token
   * @return A reversible token that contains the data
   */
  protected String cryptBytes(byte[] data) {
    final String salt = KeyGenerators.string().generateKey();

    BytesEncryptor encryptor = new BouncyCastleAesGcmBytesEncryptor(secret, salt);

    String encrypted = Base64.getUrlEncoder().encodeToString(encryptor.encrypt(data));

    return salt + "." + encrypted;
  }

  /**
   * Recovers the data from a token, as a byte array
   *
   * @param token A token (key) obtained from this TokenService
   * @return The original data as a byte array
   * @throws NullPointerException if token is null
   * @throws IllegalStateException if data cannot be decrypted using this instance's secret
   * @throws ArrayIndexOutOfBoundsException if token is malformed
   */
  protected byte[] decryptBytes(String token) {
    String[] parts = token.split("\\.");
    String salt = parts[0];
    String encrypted = parts[1];

    BytesEncryptor decryptor = new BouncyCastleAesGcmBytesEncryptor(secret, salt);

    return decryptor.decrypt(Base64.getUrlDecoder().decode(encrypted));
  }
}
