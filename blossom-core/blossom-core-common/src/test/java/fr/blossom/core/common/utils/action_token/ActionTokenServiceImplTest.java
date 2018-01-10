package fr.blossom.core.common.utils.action_token;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.token.DefaultToken;
import org.springframework.security.core.token.TokenService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActionTokenServiceImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private ActionTokenServiceImpl service;
  private TokenService tokenService;

  @Before
  public void setUp() throws Exception {
    this.tokenService = mock(TokenService.class);
    this.service = new ActionTokenServiceImpl(tokenService);
  }

  @Test
  public void should_encrypt_action_token_to_string() throws Exception {
    Instant time = Instant.now().plus(1, ChronoUnit.HOURS);

    ActionToken action = new ActionToken();
    action.setAction("test");
    action.setUserId(1L);
    action.setExpirationDate(time);
    action.setAdditionalParameters(Maps.newHashMap());

    String result = service.encryptTokenAsString(action);

    List<String> splitted = Splitter.on("|").splitToList(result);
    assertEquals(splitted.get(0), action.getUserId().toString());
    assertEquals(splitted.get(1), action.getAction());
    assertEquals(splitted.get(2), time.toEpochMilli() + "");
    assertTrue(splitted.get(3).length() == 0);
  }

  @Test
  public void should_fail_on_empty_action_in_encrypt_action_token() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    ActionToken action = new ActionToken();
    action.setAction(null);
    action.setUserId(1L);
    action.setExpirationDate(Instant.now().plus(1, ChronoUnit.HOURS));
    action.setAdditionalParameters(Maps.newHashMap());

    service.encryptTokenAsString(action);
  }

  @Test
  public void should_fail_on_empty_userId_in_encrypt_action_token() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    ActionToken action = new ActionToken();
    action.setAction("test");
    action.setUserId(null);
    action.setExpirationDate(Instant.now().plus(1, ChronoUnit.HOURS));
    action.setAdditionalParameters(Maps.newHashMap());

    service.encryptTokenAsString(action);
  }

  @Test
  public void should_fail_on_empty_expirationDate_in_encrypt_action_token() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    ActionToken action = new ActionToken();
    action.setAction("test");
    action.setUserId(1L);
    action.setExpirationDate(null);
    action.setAdditionalParameters(Maps.newHashMap());

    service.encryptTokenAsString(action);
  }

  @Test
  public void should_fail_on_null_value_in_decrypt_action_token() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    service.decryptTokenFromString(null);
  }

  @Test
  public void should_fail_on_empty_value_in_decrypt_action_token() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    service.decryptTokenFromString("");
  }

  @Test
  public void should_fail_on_badly_formatted_value_in_decrypt_action_token() throws Exception {
    thrown.expect(IllegalStateException.class);

    service.decryptTokenFromString("aksdfqkf<fbusycbjsvbkqsbvkj");
  }

  @Test
  public void should_fail_on_wrong_part_count_in_value_in_decrypt_action_token() throws Exception {
    thrown.expect(IllegalStateException.class);

    service.decryptTokenFromString("1|test|toto");
  }

  @Test
  public void should_succeed_on_wrong_part_count_in_value_in_decrypt_action_token()
    throws Exception {
    Long userId = 1L;
    String action = "test";
    Long timestamp = LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC).toEpochMilli();

    ActionToken token = service
      .decryptTokenFromString(userId + "|" + action + "|" + timestamp + "|");

    assertEquals(token.getUserId(), userId);
    assertEquals(token.getAction(), action);
    assertEquals(token.getExpirationDate().toEpochMilli(), (long) timestamp);
    assertTrue(token.isValid());
  }


  @Test
  public void should_decrypt_null_additional_parameters() throws Exception {
    Map<String, String> result = service.decryptAdditionalParameters(null);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void should_decrypt_empty_additional_parameters() throws Exception {
    Map<String, String> result = service.decryptAdditionalParameters("");
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void should_decrypt_one_additional_parameters() throws Exception {
    Map<String, String> result = service.decryptAdditionalParameters("test&=&test");
    assertNotNull(result);
    assertTrue(result.containsKey("test"));
    assertEquals("test", result.get("test"));
  }

  @Test
  public void should_decrypt_multiple_additional_parameters() throws Exception {
    Map<String, String> result = service.decryptAdditionalParameters("test&=&test#test2&=&test2");
    assertNotNull(result);
    assertTrue(result.containsKey("test"));
    assertEquals("test", result.get("test"));
    assertTrue(result.containsKey("test2"));
    assertEquals("test2", result.get("test2"));
  }

  @Test
  public void should_decrypt_badly_formatted_additional_parameters() throws Exception {
    thrown.expect(IllegalStateException.class);
    service.decryptAdditionalParameters("test&=&test#test2");
  }

  @Test
  public void should_encrypt_null_additional_parameters() throws Exception {
    String result = service.encryptAdditionalParameters(null);
    assertNotNull(result);
    assertTrue(result.length() == 0);
  }

  @Test
  public void should_encrypt_empty_additional_parameters() throws Exception {
    String result = service.encryptAdditionalParameters(Maps.newHashMap());
    assertNotNull(result);
    assertTrue(result.length() == 0);
  }

  @Test
  public void should_encrypt_one_additional_parameters() throws Exception {
    Map<String, String> map = ImmutableMap.<String, String>builder().put("test", "test").build();
    String result = service.encryptAdditionalParameters(map);
    assertNotNull(result);
    assertEquals(result, "test&=&test");
  }

  @Test
  public void should_encrypt_multiple_additional_parameters() throws Exception {
    Map<String, String> map = ImmutableMap.<String, String>builder().put("test", "test")
      .put("test2", "test2").build();
    String result = service.encryptAdditionalParameters(map);
    assertNotNull(result);
    assertTrue(result.contains("test&=&test"));
    assertTrue(result.contains("test2&=&test2"));
  }

  @Test
  public void should_generate_valid_token() throws Exception {
    ActionToken action = new ActionToken();
    action.setAction("test");
    action.setUserId(1L);
    action.setExpirationDate(Instant.now().plus(1, ChronoUnit.HOURS));
    action
      .setAdditionalParameters(ImmutableMap.<String, String>builder().put("test", "test").build());

    String expectedToken = "token !";
    when(tokenService.allocateToken(any(String.class))).thenAnswer(
      arg -> new DefaultToken(expectedToken, System.currentTimeMillis(),
        arg.getArgumentAt(0, String.class)));

    String token = this.service.generateToken(action);

    assertNotNull(token);
    assertEquals(token, expectedToken);
  }

  @Test
  public void should_decrypt_valid_token() throws Exception {
    Long userId = 1L;
    String action = "test";
    Long timestamp = LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC).toEpochMilli();

    ActionToken token = service
      .decryptTokenFromString(userId + "|" + action + "|" + timestamp + "|");

    assertNotNull(token);
    assertEquals(token.getUserId(), userId);
    assertEquals(token.getAction(), action);
    assertEquals(token.getExpirationDate().toEpochMilli(), (long) timestamp);
    assertTrue(token.isValid());
  }

  @Test
  public void should_decrypt_invalid_token() throws Exception {
    Long userId = 1L;
    String action = "test";
    Long timestamp = LocalDateTime.now().minusHours(1).toInstant(ZoneOffset.UTC).toEpochMilli();

    when(this.tokenService.verifyToken(anyString())).thenAnswer(
      arg -> new DefaultToken(arg.getArgumentAt(0, String.class), System.currentTimeMillis(),
        arg.getArgumentAt(0, String.class)));
    ActionToken token = service.decryptToken(userId + "|" + action + "|" + timestamp + "|");

    assertNotNull(token);
    assertEquals(token.getUserId(), userId);
    assertEquals(token.getAction(), action);
    assertEquals(token.getExpirationDate().toEpochMilli(), (long) timestamp);
    assertFalse(token.isValid());
  }
}
