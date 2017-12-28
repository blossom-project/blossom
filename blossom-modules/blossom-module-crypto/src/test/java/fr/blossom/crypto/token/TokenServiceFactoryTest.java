package fr.blossom.crypto.token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.SecureRandom;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceFactoryTest {

  private TokenServiceFactory factory;

  @Before
  public void setUp() {
    factory = new TokenServiceFactory(new SecureRandom());
  }

  @Test
  public void should_generateSecret_not_return_null() {
    assertNotNull(factory.generateSecret());
  }

  @Test
  public void should_generateSecret_not_return_empty() {
    assertTrue(!"".equals(factory.generateSecret()));
  }

  @Test
  public void should_generateSecret_not_return_fixed_output() {
    assertNotEquals(factory.generateSecret(), factory.generateSecret());
  }

  @Test(expected = Exception.class)
  public void should_statelessSecretTokenService_not_accept_null_secret() {
    factory.statelessSecretTokenService(null);
  }

  @Test
  public void should_statelessSecretTokenService_not_return_null() {
    assertNotNull(factory.statelessSecretTokenService(factory.generateSecret()));
  }
}