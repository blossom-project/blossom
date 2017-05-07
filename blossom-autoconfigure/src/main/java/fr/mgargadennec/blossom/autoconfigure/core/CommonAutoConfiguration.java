package fr.mgargadennec.blossom.autoconfigure.core;

import com.google.common.collect.Sets;
import fr.mgargadennec.blossom.core.common.mapper.MapperPlugin;
import fr.mgargadennec.blossom.core.common.message_source.MultiClasspathReloadableResourceBundleMessageSource;
import fr.mgargadennec.blossom.core.common.service.ReadOnlyServicePlugin;
import fr.mgargadennec.blossom.core.common.utils.action_token.ActionTokenService;
import fr.mgargadennec.blossom.core.common.utils.action_token.ActionTokenServiceImpl;
import fr.mgargadennec.blossom.core.common.utils.mail.MailSender;
import fr.mgargadennec.blossom.core.common.utils.mail.MailSenderImpl;
import fr.mgargadennec.blossom.core.common.utils.mail.NoopMailSenderImpl;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnablePluginRegistries({MapperPlugin.class, ReadOnlyServicePlugin.class})
@EnableJpaAuditing
@PropertySource("classpath:/freemarker.properties")
public class CommonAutoConfiguration {

  @Configuration
  @ConditionalOnMissingBean(MailSender.class)
  public static class MailConfiguration {
    @Bean
    @ConditionalOnBean(JavaMailSender.class)
    public MailSender mailSender(JavaMailSender javaMailSender, freemarker.template.Configuration configuration) {
      return new MailSenderImpl(javaMailSender, configuration, Sets.newHashSet(), "blossom@cardiweb.com", "http://localhost:8080");
    }

    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public MailSender noopMailSender() {
      return new NoopMailSenderImpl();
    }
  }

  @Bean
  @ConditionalOnMissingBean(SecureRandom.class)
  public SecureRandom secureRandom() {
    try {
      return SecureRandom.getInstance("SHA1PRNG");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Can't find the SHA1PRNG algorithm for generating random numbers", e);
    }
  }

  @Bean
  @ConditionalOnMissingBean(KeyBasedPersistenceTokenService.class)
  public KeyBasedPersistenceTokenService keyBasedPersistenceTokenService() {
    KeyBasedPersistenceTokenService keyBasedPersistenceTokenService = new KeyBasedPersistenceTokenService();
    keyBasedPersistenceTokenService.setServerInteger(secureRandom().nextInt());
    keyBasedPersistenceTokenService.setServerSecret(secureRandom().nextLong() + "");
    keyBasedPersistenceTokenService.setSecureRandom(secureRandom());

    return keyBasedPersistenceTokenService;
  }

  @Bean
  @ConditionalOnClass(ActionTokenService.class)
  public ActionTokenService actionTokenService(KeyBasedPersistenceTokenService keyBasedPersistenceTokenService) {
    return new ActionTokenServiceImpl(keyBasedPersistenceTokenService);
  }

  @Bean
  @ConditionalOnMissingBean(PasswordEncoder.class)
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10, secureRandom());
  }

  @Bean
  @ConditionalOnMissingBean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  public BulkProcessor bulkProcessor(Client client) {
    return BulkProcessor.builder(client, new BulkProcessor.Listener() {
      @Override
      public void beforeBulk(long executionId, BulkRequest request) {

      }

      @Override
      public void afterBulk(long executionId, BulkRequest request, Throwable failure) {

      }

      @Override
      public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

      }
    }).build();
  }


  @Bean
  public MultiClasspathReloadableResourceBundleMessageSource messageSource() {
    MultiClasspathReloadableResourceBundleMessageSource messageSource = new MultiClasspathReloadableResourceBundleMessageSource();
    messageSource.setBasenames("classpath*:/messages/*");
    return messageSource;
  }

  @Bean
  public AuditorAware<String> createAuditorProvider() {
    return new SecurityAuditor();
  }

  @Bean
  public AuditingEntityListener createAuditingListener() {
    return new AuditingEntityListener();
  }

  public static class SecurityAuditor implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth == null) {
        return "anonymous";
      }
      String username = auth.getName();
      return username;
    }
  }
}
