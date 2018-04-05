package com.blossomproject.ui.theme;

import com.blossomproject.core.common.PluginConstants;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.plugin.core.Plugin;

@Qualifier(value = PluginConstants.PLUGIN_THEME)
public class Theme implements org.springframework.ui.context.Theme, Plugin<String> {
  public final static String BLOSSOM_THEME_NAME="Blossom";

  private final String name;
  private final String[] aliases;
  private final Map<String,String> messages;
  private final MessageSource messageSource;

  public Theme(String name, String[] aliases, Map<String,String> messages, Set<Locale> availableLocales) {
    this.name = name;
    this.aliases = aliases;
    this.messages = messages;
    this.messageSource = buildMessageSource(messages,availableLocales);
  }

  private MessageSource buildMessageSource(Map<String,String> messages, Set<Locale> availableLocales) {
    StaticMessageSource themeMessageSource = new StaticMessageSource();
    availableLocales.forEach(l -> themeMessageSource.addMessages(messages, l));
    return themeMessageSource;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public MessageSource getMessageSource() {
    return this.messageSource;
  }

  public Map<String, String> getMessages() {
    return messages;
  }

  @Override
  public boolean supports(String delimiter) {
    return this.name.equals(delimiter) || Arrays.stream(this.aliases).filter(s -> s.equals(delimiter)).findAny().isPresent();
  }
}
