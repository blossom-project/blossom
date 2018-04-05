package com.blossomproject.ui.theme;

import static com.blossomproject.ui.theme.ThemeServlet.BLOSSOM_THEME_SCSS_SERVLET;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ThemeBuilderImpl implements ThemeBuilder, IBuild, IStylesheet, IBodyClass, ILogo {

  private final Set<Locale> availableLocales;
  private String name;
  private String[] aliases;
  private String stylesheet;
  private IScss scss;
  private String bodyClass;
  private String logo;

  public ThemeBuilderImpl(Set<Locale> availableLocales) {
    Preconditions.checkArgument(availableLocales != null);
    Preconditions.checkArgument(availableLocales.size() >= 0);
    this.availableLocales = availableLocales;
  }

  public IStylesheet name(String name) {
    this.name = name;
    this.aliases = new String[0];
    return this;
  }

  public IStylesheet nameAndAliases(String name, String... aliases) {
    this.name = name;
    this.aliases = aliases;
    return this;
  }

  public IScss scss() {
    this.stylesheet = BLOSSOM_THEME_SCSS_SERVLET;
    this.scss = new ScssImpl(this);
    return scss;
  }

  public IBuild logo(String logo) {
    this.logo = logo;
    return this;
  }

  public ILogo bodyClass(String bodyClass) {
    this.bodyClass = bodyClass;
    return this;
  }

  @Override
  public ILogo noBodyClass() {
    this.bodyClass = "";
    return this;
  }

  public Theme build() {
    return new Theme(this.name, this.aliases, buildProperties(), availableLocales);
  }

  private Map<String, String> buildProperties() {
    Map<String, String> messages = Maps.newHashMap();
    messages.put("stylesheet", this.stylesheet);
    messages.put("bodyClass", this.bodyClass);
    messages.put("navLogo", this.logo);
    messages.put("additionnalScss", this.scss.additionnalScss());

    if (this.scss != null) {
      Method[] methods = IScssVariables.class.getDeclaredMethods();
      for (Method method : methods) {
        if (method.getParameters().length == 0 && !method.getName().equals("done")) {
          try {
            messages.put(method.getName(), (String) method.invoke(this.scss.variables()));
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
    return messages;
  }


}

