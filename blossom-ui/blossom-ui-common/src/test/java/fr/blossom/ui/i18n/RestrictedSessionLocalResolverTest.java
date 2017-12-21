package fr.blossom.ui.i18n;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestrictedSessionLocalResolverTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private Set<Locale> locales;
  private RestrictedSessionLocalResolver resolver;

  @Before
  public void setUp() throws Exception {
    this.locales = Sets.newHashSet(Locale.FRANCE, Locale.ENGLISH);
    this.resolver = spy(new RestrictedSessionLocalResolver(this.locales));
  }


  @Test
  public void should_construct_not_null_locales() {
    thrown.expect(IllegalArgumentException.class);
    new RestrictedSessionLocalResolver(null);
  }

  @Test
  public void should_construct_not_empty_locales() {
    thrown.expect(IllegalArgumentException.class);
    new RestrictedSessionLocalResolver(Sets.newHashSet());
  }

  @Test
  public void should_change_to_available_locale() throws Exception {
    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getSession()).thenReturn(session);

    HttpServletResponse response = mock(HttpServletResponse.class);
    Locale locale = Locale.FRANCE;

    this.resolver.setLocale(request, response, locale);
    verify(this.resolver, times(1)).doSetLocale(eq(request), eq(response), eq(Locale.FRANCE));

  }

  @Test
  public void should_change_to_unavailable_locale_find_closest() throws Exception {
    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getSession()).thenReturn(session);

    HttpServletResponse response = mock(HttpServletResponse.class);
    Locale locale = Locale.CANADA;

    this.resolver.setLocale(request, response, locale);
    verify(this.resolver, times(1))
      .doSetLocale(eq(request), eq(response), eq(Locale.ENGLISH));
  }

  @Test
  public void should_change_to_unavailable_locale_without_default() throws Exception {
    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getSession()).thenReturn(session);

    HttpServletResponse response = mock(HttpServletResponse.class);
    Locale locale = Locale.CHINA;

    this.resolver.setLocale(request, response, locale);
    verify(this.resolver, times(1))
      .doSetLocale(eq(request), eq(response), eq(Iterables.getFirst(this.locales, null)));
  }

  @Test
  public void should_change_to_unavailable_locale_with_default() throws Exception {
    HttpSession session = mock(HttpSession.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getSession()).thenReturn(session);

    HttpServletResponse response = mock(HttpServletResponse.class);
    Locale locale = Locale.CHINA;

    this.resolver.setDefaultLocale(Locale.FRANCE);
    this.resolver.setLocale(request, response, locale);
    verify(this.resolver, times(1)).doSetLocale(eq(request), eq(response), eq(Locale.FRANCE));
  }
}
