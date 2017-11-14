package fr.blossom.ui.i18n;

import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Maël Gargadennnec on 05/06/2017.
 */
public class RestrictedSessionLocalResolver extends SessionLocaleResolver {
  private final Set<Locale> availableLocales;

  public RestrictedSessionLocalResolver(Set<Locale> avaialableLocales) {
    this.availableLocales = avaialableLocales;
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    if (availableLocales.contains(locale)) {
      super.setLocale(request, response, locale);
    } else {
      Optional<Locale> closest= availableLocales.stream().filter(aLocale -> aLocale.getLanguage().equals(locale.getLanguage())).findFirst();
      if(closest.isPresent()){
        super.setLocale(request, response, closest.get());
        return;
      }
      super.setLocale(request, response, getDefaultLocale());
    }
  }
}
