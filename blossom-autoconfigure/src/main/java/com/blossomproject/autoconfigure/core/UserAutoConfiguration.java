package com.blossomproject.autoconfigure.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.blossomproject.core.cache.CacheConfig;
import com.blossomproject.core.cache.CacheConfig.CacheConfigBuilder;
import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.IndexationEngineConfiguration;
import com.blossomproject.core.common.search.IndexationEngineImpl;
import com.blossomproject.core.common.search.SearchEngineConfiguration;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SummaryDTO;
import com.blossomproject.core.common.search.SummaryDTO.SummaryDTOBuilder;
import com.blossomproject.core.common.service.AssociationServicePlugin;
import com.blossomproject.core.common.utils.action_token.ActionTokenService;
import com.blossomproject.core.common.utils.mail.MailSender;
import com.blossomproject.core.user.User;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserDTOMapper;
import com.blossomproject.core.user.UserDao;
import com.blossomproject.core.user.UserDaoImpl;
import com.blossomproject.core.user.UserIndexationJob;
import com.blossomproject.core.user.UserMailService;
import com.blossomproject.core.user.UserMailServiceImpl;
import com.blossomproject.core.user.UserRepository;
import com.blossomproject.core.user.UserService;
import com.blossomproject.core.user.UserServiceImpl;
import java.io.IOException;
import java.util.function.Function;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnClass(User.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EntityScan(basePackageClasses = User.class)
public class UserAutoConfiguration {

  @Qualifier(PluginConstants.PLUGIN_ASSOCIATION_SERVICE)
  @Autowired
  private PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationServicePlugins;


  @Bean
  @ConditionalOnMissingBean(UserMailService.class)
  public UserMailService userMailService(MailSender mailSender) {
    return new UserMailServiceImpl(mailSender);
  }

  @Bean
  @ConditionalOnMissingBean(UserService.class)
  public UserService userService(UserDao userDao, UserDTOMapper userDTOMapper,
    PasswordEncoder passwordEncoder, ActionTokenService actionTokenService,
    UserMailService userMailService,
    ApplicationEventPublisher eventPublisher,
    @Value("classpath:/images/avatar.jpeg") Resource defaultAvatarFile) throws IOException {
    if (!defaultAvatarFile.exists()) {
      throw new RuntimeException("Cannot find default user avatar on the classpath.");
    }
    return new UserServiceImpl(userDao, userDTOMapper, eventPublisher, associationServicePlugins,
      passwordEncoder,
      actionTokenService, userMailService, ByteStreams.toByteArray(defaultAvatarFile.getInputStream()));
  }

  @Bean
  @ConditionalOnMissingBean(UserDao.class)
  public UserDao userDao(UserRepository userRepository) {
    return new UserDaoImpl(userRepository);
  }

  @Bean
  @ConditionalOnMissingBean(UserDTOMapper.class)
  public UserDTOMapper userDTOMapper() {
    return new UserDTOMapper();
  }

  @Bean
  @ConditionalOnMissingBean(name = "userDaoCacheConfig")
  public CacheConfig userDaoCacheConfig() {
    return CacheConfigBuilder.create(UserDaoImpl.class.getCanonicalName())
      .specification("expireAfterWrite=15m").build();
  }

  @Bean
  public IndexationEngineConfiguration<UserDTO> userIndexationEngineConfiguration(
    @Value("classpath:/elasticsearch/users.json") Resource resource) {
    return new IndexationEngineConfiguration<UserDTO>() {
      @Override
      public Class<UserDTO> getSupportedClass() {
        return UserDTO.class;
      }

      @Override
      public Resource getSource() {
        return resource;
      }

      @Override
      public String getAlias() {
        return "users";
      }

      @Override
      public Function<UserDTO, String> getTypeFunction() {
        return u -> "user";
      }

      @Override
      public Function<UserDTO, SummaryDTO> getSummaryFunction() {
        return u -> SummaryDTOBuilder.create().id(u.getId()).type(this.getTypeFunction().apply(u))
          .name(u.getFirstname() + " " + u.getLastname()).description(u.getDescription())
          .uri("/blossom/administration/users/" + u.getId()).build();
      }
    };
  }

  @Bean
  public IndexationEngineImpl<UserDTO> userIndexationEngine(Client client,
    UserService userService,
    BulkProcessor bulkProcessor,
    ObjectMapper objectMapper,
    IndexationEngineConfiguration<UserDTO> userIndexationEngineConfiguration) {
    return new IndexationEngineImpl<>(client, userService, bulkProcessor, objectMapper,
      userIndexationEngineConfiguration);
  }


  @Bean
  public SearchEngineConfiguration<UserDTO> userSearchEngineConfiguration() {
    return new SearchEngineConfiguration<UserDTO>() {
      @Override
      public String getName() {
        return "menu.administration.users";
      }

      @Override
      public Class<UserDTO> getSupportedClass() {
        return UserDTO.class;
      }

      @Override
      public String[] getFields() {
        return new String[]{"dto.identifier", "dto.email", "dto.firstname", "dto.lastname",
          "dto.company", "dto.description", "dto.function"};
      }

      @Override
      public String getAlias() {
        return "users";
      }
    };
  }

  @Bean
  public SearchEngineImpl<UserDTO> userSearchEngine(Client client, ObjectMapper objectMapper,
    SearchEngineConfiguration<UserDTO> userSearchEngineConfiguration) {
    return new SearchEngineImpl<>(client, objectMapper, userSearchEngineConfiguration);
  }

  @Bean
  @Qualifier("userIndexationFullJob")
  public JobDetailFactoryBean userIndexationFullJob() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(UserIndexationJob.class);
    factoryBean.setGroup("Indexation");
    factoryBean.setName("Users Indexation Job");
    factoryBean.setDescription("Users full indexation Job");
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean
  @Qualifier("userScheduledIndexationTrigger")
  public SimpleTriggerFactoryBean userScheduledIndexationTrigger(
    @Qualifier("userIndexationFullJob") JobDetail userIndexationFullJob) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("User re-indexation");
    factoryBean.setDescription("Periodic re-indexation of all users of the application");
    factoryBean.setJobDetail(userIndexationFullJob);
    factoryBean.setStartDelay((long) 30 * 1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(
      SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }

}
