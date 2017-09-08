package fr.mgargadennec.blossom.autoconfigure.core;

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

import com.google.common.collect.Sets;

import fr.mgargadennec.blossom.core.common.utils.mail.MailSender;
import fr.mgargadennec.blossom.core.common.utils.mail.MailSenderImpl;
import fr.mgargadennec.blossom.core.common.utils.mail.NoopMailSenderImpl;

@Configuration
@ConditionalOnMissingBean(MailSender.class)
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@PropertySource({"classpath:/mailsender.properties"})
public class MailAutoConfiguration {

  @Value("${blossom.mail.url}")
  String baseUrlForMail;

  @Bean
  @ConditionalOnBean(JavaMailSender.class)
  public MailSender blossomMailSender(JavaMailSender javaMailSender, MessageSource messageSource,
      freemarker.template.Configuration configuration) {
    return new MailSenderImpl(javaMailSender, configuration, Sets.newHashSet(".*"), messageSource,
        "blossom@blossom.fr", baseUrlForMail);
  }

  @Bean
  @ConditionalOnMissingBean(JavaMailSender.class)
  public MailSender blossomNoopMailSender(freemarker.template.Configuration freemarkerConfiguration) {
    return new NoopMailSenderImpl();
  }

}
