package com.blossomproject.ui.i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
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
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class RestrictedSessionLocaleResolverTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private Set<Locale> locales;
  private RestrictedSessionLocaleResolver resolver;

  @Before
  public void setUp() throws Exception {
    this.locales = Sets.newHashSet(Locale.FRANCE, Locale.ENGLISH);
    this.resolver = spy(new RestrictedSessionLocaleResolver(this.locales));
  }


  @Test
  public void should_construct_not_null_locales() {
    thrown.expect(IllegalArgumentException.class);
    new RestrictedSessionLocaleResolver(null);
  }

  @Test
  public void should_construct_not_empty_locales() {
    thrown.expect(IllegalArgumentException.class);
    new RestrictedSessionLocaleResolver(Sets.newHashSet());
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

  @Test
  public void should_determine_default_locale_if_available() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    Locale locale = Locale.FRANCE;
    when(request.getLocale()).thenReturn(locale);

    Locale response = this.resolver.determineDefaultLocale(request);

    assertNotNull(response);
    assertEquals(locale, response);

    verify(request, times(1)).getLocale();
  }

  @Test
  public void should_determine_default_locale_with_request_locale_null() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getLocale()).thenReturn(null);

    Locale response = this.resolver.determineDefaultLocale(request);

    assertNull(response);
  }

  @Test
  public void should_determine_default_locale_if_no_closest_local() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    Locale locale = Locale.CHINA;
    when(request.getLocale()).thenReturn(locale);

    Locale response = this.resolver.determineDefaultLocale(request);

    assertNotNull(response);
    assertEquals(locale, response);
  }

  @Test
  public void should_determine_default_locale_if_closest_local() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    Locale locale = Locale.FRENCH;
    when(request.getLocale()).thenReturn(locale);

    Locale response = this.resolver.determineDefaultLocale(request);

    assertNotNull(response);
    assertEquals(response, Locale.FRANCE);
  }
}
