package com.blossomproject.ui.i18n;

import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Locale;
import java.util.Set;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LocaleControllerAdviceTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private Set<Locale> locales;
  private LocaleControllerAdvice advice;

  @Before
  public void setUp() throws Exception {
    this.locales = Sets.newHashSet(Locale.FRANCE, Locale.ENGLISH);
    this.advice = new LocaleControllerAdvice(locales);
  }

  @Test
  public void should_construct_not_null_locales() {
    thrown.expect(IllegalArgumentException.class);
    new LocaleControllerAdvice(null);
  }

  @Test
  public void should_construct_not_empty_locales() {
    thrown.expect(IllegalArgumentException.class);
    new LocaleControllerAdvice(Sets.newHashSet());
  }

  @Test
  public void languages() throws Exception {
    assertTrue(
      this.advice.languages().containsAll(Lists.newArrayList(Locale.FRANCE, Locale.ENGLISH)));
  }

  @Test
  public void currentLanguage() throws Exception {
    assertTrue(this.advice.currentLanguage(Locale.FRANCE).equals(Locale.FRANCE));
  }

}
