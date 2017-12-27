package fr.blossom.crypto.token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.token.Token;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StatelessSecretTokenServiceTest {

  private TokenServiceFactory tokenServiceFactory;
  private StatelessSecretTokenService statelessSecretTokenService;
  private String testData;

  @Before
  public void setUp() {
    tokenServiceFactory = new TokenServiceFactory(new SecureRandom());
    statelessSecretTokenService = new StatelessSecretTokenService(tokenServiceFactory.generateSecret());
    testData = tokenServiceFactory.generateSecret(); // Here the "secret" is used as random data to encrypt/decrypt
  }

  @Test(expected = Exception.class)
  public void should_not_accept_null_secret() {
    new StatelessSecretTokenService(null);
  }

  @Test
  public void should_encrypt_bytes_not_return_null() {
    String token = statelessSecretTokenService.cryptBytes(testData.getBytes(StandardCharsets.UTF_8));
    assertNotNull(token);
  }

  @Test
  public void should_encrypt_bytes_not_contain_unencrypted_data() {
    String token = statelessSecretTokenService.cryptBytes(testData.getBytes(StandardCharsets.UTF_8));
    assertNotEquals(token, testData);
    assertFalse(token.contains(testData));
    assertFalse(token.contains(Base64.getEncoder().encodeToString(testData.getBytes(StandardCharsets.UTF_8))));
  }

  @Test
  public void should_decrypt_bytes_return_original_data() {
    String token = statelessSecretTokenService.cryptBytes(testData.getBytes(StandardCharsets.UTF_8));
    assertEquals(testData, new String(statelessSecretTokenService.decryptBytes(token), StandardCharsets.UTF_8));
  }

  @Test
  public void should_encrypt_string_not_return_null() {
    Token token = statelessSecretTokenService.allocateToken(testData);
    assertNotNull(token);
    assertNotNull(token.getKey());
  }

  @Test
  public void should_encrypt_string_not_contain_original_data() {
    Token token = statelessSecretTokenService.allocateToken(testData);
    assertNotEquals(token.getKey(), testData);
    assertFalse(token.getKey().contains(testData));
  }

  @Test
  public void should_decrypt_string_return_original_data() {
    Token tokenIn = statelessSecretTokenService.allocateToken(testData);
    Token tokenOut = statelessSecretTokenService.verifyToken(tokenIn.getKey());
    assertEquals(testData, tokenOut.getExtendedInformation());
  }

  @Test
  public void should_decrypt_string_return_original_creation_date() {
    Token tokenIn = statelessSecretTokenService.allocateToken(testData);
    Token tokenOut = statelessSecretTokenService.verifyToken(tokenIn.getKey());
    assertEquals(tokenIn.getKeyCreationTime(), tokenOut.getKeyCreationTime());
  }

}