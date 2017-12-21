package fr.blossom.ui.security;

import static junit.framework.TestCase.assertTrue;

import java.util.Map;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginAttemptServiceImplTest {

  private LoginAttemptServiceImpl loginAttemptService;

  @Before
  public void setUp() throws Exception {
    this.loginAttemptService = new LoginAttemptServiceImpl(5);
  }

  @Test
  public void successfulAttempt() throws Exception {
    this.loginAttemptService.failAttempt("test", "test");
    this.loginAttemptService.successfulAttempt("test", "test");

    Map<String, Map<String, Integer>> attempts = this.loginAttemptService.get();
    assertTrue(attempts.isEmpty());
  }

  @Test
  public void failAttempt() throws Exception {
    String identifier = "testIdentifier";
    String ip = "testIP";

    this.loginAttemptService.failAttempt(identifier, ip);

    Map<String, Map<String, Integer>> attempts = this.loginAttemptService.get();
    assertTrue(!attempts.isEmpty());
    assertTrue(attempts.containsKey(identifier));
    assertTrue(attempts.get(identifier).containsKey(ip));
    assertTrue(attempts.get(identifier).get(ip) == 1);
  }

  @Test
  public void isBlocked() throws Exception {
    String identifier = "testIdentifier";
    String ip = "testIP";
    IntStream.range(0, 50).forEach(v -> this.loginAttemptService.failAttempt(identifier, ip));
    boolean blocked = this.loginAttemptService.isBlocked(identifier, ip);
    assertTrue(blocked);
  }

  @Test
  public void get_empty() throws Exception {
    Map<String, Map<String, Integer>> attempts =this.loginAttemptService.get();
    assertTrue(attempts.isEmpty());
  }

  @Test
  public void get_not_empty() throws Exception {
    String identifier = "testIdentifier";
    String ip = "testIP";

    this.loginAttemptService.failAttempt(identifier, ip);

    Map<String, Map<String, Integer>> attempts =this.loginAttemptService.get();
    assertTrue(!attempts.isEmpty());
    assertTrue(attempts.containsKey(identifier));
    assertTrue(attempts.get(identifier).containsKey(ip));
    assertTrue(attempts.get(identifier).get(ip) == 1);
  }

}
