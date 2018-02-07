package com.blossom_project.core.common.entity.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class LocaleConverterTest {

  private LocaleConverter localeConverter;

  @Before
  public void setUp() throws Exception {
    this.localeConverter = new LocaleConverter();
  }

  @Test
  public void should_serialize_null() throws Exception {
    String result = this.localeConverter.convertToDatabaseColumn(null);
    assertNull(result);
  }

  @Test
  public void should_serialize_locale() throws Exception {
    Locale locale = Locale.FRANCE;
    String result = this.localeConverter.convertToDatabaseColumn(locale);
    assertNotNull(result);
    assertEquals(result, locale.toLanguageTag());
  }

  @Test
  public void should_deserialize_null() throws Exception {
    Locale result = this.localeConverter.convertToEntityAttribute(null);
    assertNull(result);
  }

  @Test
  public void should_deserialize_locale() throws Exception {
    String localeSTR = "fr-FR";
    Locale result = this.localeConverter.convertToEntityAttribute(localeSTR);
    assertNotNull(result);
    assertEquals(result.toLanguageTag(), localeSTR);
  }

  @Test
  public void should_return_undetermined_on_undertermined() throws Exception {
    String localeSTR = "sffbgdvwvqdvsdv";
    Locale result = this.localeConverter.convertToEntityAttribute(localeSTR);
    assertNotNull(result);
    assertEquals(result.toLanguageTag(), "und");
  }
}
