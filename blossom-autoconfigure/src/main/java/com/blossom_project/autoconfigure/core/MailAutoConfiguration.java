package com.blossom_project.autoconfigure.core;

import com.google.common.collect.Iterables;
import com.blossom_project.core.common.utils.mail.MailSender;
import com.blossom_project.core.common.utils.mail.MailSenderImpl;
import com.blossom_project.core.common.utils.mail.NoopMailSenderImpl;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@ConditionalOnMissingBean(MailSender.class)
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@PropertySource({"classpath:/mailsender.properties"})
public class MailAutoConfiguration {

  @Value("${blossom.mail.url}")
  String baseUrl;

  @Value("${blossom.mail.from}")
  String from;

  @Value("${blossom.mail.filters}")
  Set<String> filters;

  @Bean
  @ConditionalOnBean(JavaMailSender.class)
  @ConditionalOnMissingBean(MailSender.class)
  public MailSender blossomMailSender(JavaMailSender javaMailSender, MessageSource messageSource,
    freemarker.template.Configuration configuration, Set<Locale> availableLocales) {
    return new MailSenderImpl(javaMailSender,
      configuration,
      filters,
      messageSource,
      from,
      baseUrl,
      Iterables.getFirst(availableLocales, Locale.ENGLISH));
  }

  @Bean
  @ConditionalOnMissingBean(JavaMailSender.class)
  public MailSender blossomNoopMailSender() {
    return new NoopMailSenderImpl();
  }

}
