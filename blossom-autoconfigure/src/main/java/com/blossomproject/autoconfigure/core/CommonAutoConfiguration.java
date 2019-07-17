package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.common.mapper.MapperPlugin;
import com.blossomproject.core.common.service.AssociationServicePlugin;
import com.blossomproject.core.common.service.ReadOnlyServicePlugin;
import com.blossomproject.core.common.utils.action_token.ActionTokenService;
import com.blossomproject.core.common.utils.action_token.ActionTokenServiceImpl;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore(ElasticsearchAutoConfiguration.class)
@EnablePluginRegistries({
  MapperPlugin.class, ReadOnlyServicePlugin.class, AssociationServicePlugin.class, Privilege.class})
@EnableJpaAuditing
@PropertySource({
  "classpath:/freemarker.properties",
  "classpath:/jpa.properties",
  "classpath:/languages.properties"})
@EnableTransactionManagement
public class CommonAutoConfiguration {

  private final static Logger logger = LoggerFactory.getLogger(CommonAutoConfiguration.class);

  @Bean
  public Set<Locale> availableLocales(@Value("${blossom.languages}") String[] languages) {
    return Stream.of(languages).sequential().map(language -> Locale.forLanguageTag(language))
      .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  @Bean
  @ConditionalOnMissingBean(TokenService.class)
  public KeyBasedPersistenceTokenService keyBasedPersistenceTokenService(
    SecureRandom secureRandom) {
    KeyBasedPersistenceTokenService keyBasedPersistenceTokenService = new KeyBasedPersistenceTokenService();
    keyBasedPersistenceTokenService.setServerInteger(secureRandom.nextInt());
    keyBasedPersistenceTokenService.setServerSecret(secureRandom.nextLong() + "");
    keyBasedPersistenceTokenService.setSecureRandom(secureRandom);

    return keyBasedPersistenceTokenService;
  }

  @Bean
  @ConditionalOnClass(ActionTokenService.class)
  public ActionTokenService defaultActionTokenService(
    TokenService tokenService) {
    return new ActionTokenServiceImpl(tokenService);
  }

  @Bean
  @ConditionalOnMissingBean(PasswordEncoder.class)
  public PasswordEncoder passwordEncoder(SecureRandom secureRandom) {
    return new BCryptPasswordEncoder(10, secureRandom);
  }

  @Bean
  public BeanPostProcessor blossomMessageSourcePostProcessor(
    BlossomReloadableResourceBundleMessageSource parentMessageSource) {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
      }

      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ResourceBundleMessageSource && beanName.equals("messageSource")) {
          ((ResourceBundleMessageSource) bean).setParentMessageSource(parentMessageSource);
        }
        return bean;
      }
    };
  }

  @Bean
  public BlossomReloadableResourceBundleMessageSource blossomMessageSource() throws IOException {
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Set<String> resources = Lists
      .newArrayList(resolver.getResources("classpath*:/messages/*.properties")).stream()
      .filter(resource -> !resource.getFilename().contains("_"))
      .map(resource -> "classpath*:/messages/" + resource.getFilename().replace(".properties", ""))
      .collect(Collectors.toSet());

    logger.info("Found {} i18n files :\n{}", resources.size(), Joiner.on(",").join(resources));

    BlossomReloadableResourceBundleMessageSource messageSource = new BlossomReloadableResourceBundleMessageSource();
    messageSource.setBasenames(resources.toArray(new String[resources.size()]));
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setCacheSeconds(3600);
    messageSource.setDefaultEncoding(Charset.forName("UTF-8").displayName());
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

  @Bean
  public Tika tika() {
    return new Tika();
  }

  public static class SecurityAuditor implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth == null) {
        return Optional.of("anonymous");
      }
      String username = auth.getName();
      Optional<? extends GrantedAuthority> switchUserAuthorities = auth.getAuthorities().stream().filter(a -> a instanceof SwitchUserGrantedAuthority).findAny();
      if (switchUserAuthorities.isPresent()) {
        username = ((SwitchUserGrantedAuthority) switchUserAuthorities.get()).getSource().getName();
      }
      return Optional.of(username);
    }
  }
}
