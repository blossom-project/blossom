package fr.blossom.autoconfigure.core;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import fr.blossom.core.common.mapper.MapperPlugin;
import fr.blossom.core.common.service.AssociationServicePlugin;
import fr.blossom.core.common.service.ReadOnlyServicePlugin;
import fr.blossom.core.common.utils.action_token.ActionTokenService;
import fr.blossom.core.common.utils.action_token.ActionTokenServiceImpl;
import fr.blossom.core.common.utils.privilege.Privilege;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.tika.Tika;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
  @ConditionalOnMissingBean(BulkProcessor.class)
  public BulkProcessor bulkProcessor(Client client) {
    return BulkProcessor.builder(client, new BulkProcessor.Listener() {

      @Override
      public void beforeBulk(long executionId, BulkRequest request) {
        logger.info("Before bulk {} with {} actions to execute", executionId,
          request.numberOfActions());
      }

      @Override
      public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
        logger.error("Error on bulk {} with {} actions to execute", executionId,
          request.numberOfActions(), failure);
      }

      @Override
      public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
        logger.info("Successful bulk {} with {} actions executed in {} ms.", executionId,
          request.numberOfActions(), response.getTookInMillis());
      }
    })
      .setName("Blossom Bulk Processor")
      .setBulkActions(500)
      .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
      .setFlushInterval(new TimeValue(30, TimeUnit.SECONDS))
      .build();
  }

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() throws IOException {
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    List<String> resources = Lists
      .newArrayList(resolver.getResources("classpath*:/messages/*.properties")).stream()
      .filter(resource -> !resource.getFilename().contains("_"))
      .map(resource -> "classpath:/messages/" + resource.getFilename().replace(".properties", ""))
      .collect(Collectors.toList());

    logger.info("Found {} i18n files :\n{}", resources.size(), Joiner.on(",").join(resources));

    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
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
      return Optional.of(username);
    }
  }
}
