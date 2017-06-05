package fr.mgargadennec.blossom.ui.i18n;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Locale;
import java.util.Set;

@ControllerAdvice
public class LocaleControllerAdvice {
  private final Set<Locale> locales;

  public LocaleControllerAdvice(Set<Locale> locales) {
    this.locales = locales;
  }

  @ModelAttribute("locales")
  public Set<Locale> languages() {
    return locales;
  }

  @ModelAttribute("currentLocale")
  public Locale currentLanguage(Locale locale) {
    return locale;
  }

}
