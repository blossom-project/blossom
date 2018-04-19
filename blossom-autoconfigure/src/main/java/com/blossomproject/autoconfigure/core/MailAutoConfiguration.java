package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.common.utils.mail.MailSender;
import com.blossomproject.core.common.utils.mail.MailSenderImpl;
import com.blossomproject.core.common.utils.mail.NoopMailSenderImpl;
import com.google.common.collect.Iterables;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Configuration
@ConditionalOnMissingBean(MailSender.class)
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailAutoConfiguration {

  @Configuration
  @ConfigurationProperties("blossom.mail")
  @PropertySource({"classpath:/mailsender.properties"})
  public static class MailsenderProperties {
    private String baseUrl;
    private String from;
    private final Set<String> filters = new HashSet<>();

    public String getBaseUrl() {
      return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
    }

    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }

    public Set<String> getFilters() {
      return filters;
    }
  }

  @Bean
  @ConditionalOnBean(JavaMailSender.class)
  @ConditionalOnMissingBean(MailSender.class)
  public MailSender blossomMailSender(JavaMailSender javaMailSender,
                                      MessageSource messageSource,
                                      freemarker.template.Configuration configuration,
                                      Set<Locale> availableLocales,
                                      MailsenderProperties properties) {
    return new MailSenderImpl(javaMailSender,
      configuration,
      properties.getFilters(),
      messageSource,
      properties.getFrom(),
      properties.getBaseUrl(),
      Iterables.getFirst(availableLocales, Locale.ENGLISH));
  }

  @Bean
  @ConditionalOnMissingBean(JavaMailSender.class)
  public MailSender blossomNoopMailSender() {
    return new NoopMailSenderImpl();
  }

}
