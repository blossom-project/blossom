package fr.blossom.core.cache;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlossomCacheManagerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_not_accept_null_environment() {
    thrown.expect(NullPointerException.class);
    new BlossomCacheManager(null,null);
  }

  @Test
  public void should_accept_environment() {
    thrown.expect(NullPointerException.class);
    new BlossomCacheManager(null,null);
  }

}
