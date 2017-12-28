package fr.blossom.core.crypto.token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.token.Token;

@RunWith(MockitoJUnitRunner.class)
public class StatelessSecretTokenServiceTest {

  private StatelessSecretTokenService statelessSecretTokenService;
  private String testData;

  @Before
  public void setUp() {
    this.testData = "testSecret";
    this.statelessSecretTokenService = new StatelessSecretTokenService(this.testData);
  }

  @Test(expected = Exception.class)
  public void should_not_accept_null_secret() {
    new StatelessSecretTokenService(null);
  }

  @Test
  public void should_encrypt_bytes_not_return_null() {
    String token = statelessSecretTokenService
      .cryptBytes(testData.getBytes(StandardCharsets.UTF_8));
    assertNotNull(token);
  }

  @Test
  public void should_encrypt_bytes_not_contain_unencrypted_data() {
    String token = statelessSecretTokenService
      .cryptBytes(testData.getBytes(StandardCharsets.UTF_8));
    assertNotEquals(token, testData);
    assertFalse(token.contains(testData));
    assertFalse(token
      .contains(Base64.getEncoder().encodeToString(testData.getBytes(StandardCharsets.UTF_8))));
  }

  @Test
  public void should_decrypt_bytes_return_original_data() {
    String token = statelessSecretTokenService
      .cryptBytes(testData.getBytes(StandardCharsets.UTF_8));
    assertEquals(testData,
      new String(statelessSecretTokenService.decryptBytes(token), StandardCharsets.UTF_8));
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
