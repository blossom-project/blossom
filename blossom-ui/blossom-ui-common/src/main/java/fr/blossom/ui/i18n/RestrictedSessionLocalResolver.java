package fr.blossom.ui.i18n;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Created by Maël Gargadennnec on 05/06/2017.
 */
public class RestrictedSessionLocalResolver extends SessionLocaleResolver {

  private final Set<Locale> availableLocales;

  public RestrictedSessionLocalResolver(Set<Locale> availableLocales) {
    Preconditions.checkArgument(availableLocales != null && !availableLocales.isEmpty());
    this.availableLocales = availableLocales;
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    if (availableLocales.contains(locale)) {
      doSetLocale(request, response, locale);
    } else {
      Optional<Locale> closest = availableLocales.stream()
        .filter(aLocale -> aLocale.getLanguage().equals(locale.getLanguage())).findFirst();
      if (closest.isPresent()) {
        doSetLocale(request, response, closest.get());
        return;
      }
      if (getDefaultLocale() != null) {
        doSetLocale(request, response, getDefaultLocale());
      } else {
        doSetLocale(request, response, availableLocales.iterator().next());
      }
    }
  }

  @Override
  protected Locale determineDefaultLocale(HttpServletRequest request) {
    Locale requestLocale = request.getLocale();
    if (requestLocale != null) {
      if (availableLocales.contains(requestLocale)) {
        return requestLocale;
      } else {
        Optional<Locale> closest = availableLocales.stream()
          .filter(aLocale -> aLocale.getLanguage().equals(requestLocale.getLanguage())).findFirst();
        if (closest.isPresent()) {
          return closest.get();
        }
      }
    }
    return super.determineDefaultLocale(request);
  }

  void doSetLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    super.setLocale(request, response, locale);
  }
}
