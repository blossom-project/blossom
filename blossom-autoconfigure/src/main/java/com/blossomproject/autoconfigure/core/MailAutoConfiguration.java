package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.common.utils.mail.*;
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

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Configuration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailAutoConfiguration {

  @Configuration
  @ConfigurationProperties("blossom.mail")
  @PropertySource({"classpath:/mailsender.properties"})
  public static class MailsenderProperties {
    private String url;
    private String from;
    private String fromName;
    private final Set<String> filters = new HashSet<>();

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }

    public String getFromName() {
      return fromName;
    }

    public void setFromName(String fromName) {
      this.fromName = fromName;
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
                                      MailsenderProperties properties, MailFilter mailFilter,
                                      AsyncMailSender asyncMailSender)
    throws UnsupportedEncodingException {

    return new MailSenderImpl(
      javaMailSender,
      configuration,
      messageSource,
      properties.getUrl(),
      Iterables.getFirst(availableLocales, Locale.ENGLISH),
      mailFilter,
      asyncMailSender,
      new InternetAddress(properties.getFrom(), properties.getFromName())
    );
  }

  @Bean
  @ConditionalOnMissingBean(MailFilter.class)
  public MailFilter blossomDefaultMailFilter(MailsenderProperties properties) {
    return new MailFilterImpl(properties.getFilters());
  }

  @Bean
  @ConditionalOnMissingBean({JavaMailSender.class, MailSender.class})
  public MailSender blossomNoopMailSender() {
    return new NoopMailSenderImpl();
  }

  @Bean("BlossomAsyncMailSender")
  @ConditionalOnMissingBean(AsyncMailSender.class)
  public AsyncMailSender blossomAsyncMailSender() {
    return new AsyncMailSenderImpl();
  }

}
