package fr.mgargadennec.blossom.autoconfigure.core;

import com.google.common.collect.Sets;
import fr.mgargadennec.blossom.core.common.utils.mail.MailSender;
import fr.mgargadennec.blossom.core.common.utils.mail.MailSenderImpl;
import fr.mgargadennec.blossom.core.common.utils.mail.NoopMailSenderImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@ConditionalOnMissingBean(MailSender.class)
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailAutoConfiguration {

  @Bean
  @ConditionalOnBean(JavaMailSender.class)
  public MailSender blossomMailSender(JavaMailSender javaMailSender, MessageSource messageSource, freemarker.template.Configuration configuration) {
    return new MailSenderImpl(javaMailSender, configuration, Sets.newHashSet(), messageSource, "blossom@cardiweb.com", "http://localhost:8080");
  }

  @Bean
  @ConditionalOnMissingBean(JavaMailSender.class)
  public MailSender blossomNoopMailSender(freemarker.template.Configuration freemarkerConfiguration) {
    return new NoopMailSenderImpl(freemarkerConfiguration, "http://localhost:8080");
  }

}
