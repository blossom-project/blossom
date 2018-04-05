package com.blossomproject.ui.theme;

public interface ThemeBuilder {

  IStylesheet name(String name);

  IStylesheet nameAndAliases(String name, String... aliases);

}
