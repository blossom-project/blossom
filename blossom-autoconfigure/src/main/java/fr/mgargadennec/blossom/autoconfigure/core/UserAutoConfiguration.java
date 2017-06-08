package fr.mgargadennec.blossom.autoconfigure.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.core.common.search.IndexationEngineImpl;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.common.utils.action_token.ActionTokenService;
import fr.mgargadennec.blossom.core.common.utils.mail.MailSender;
import fr.mgargadennec.blossom.core.user.*;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
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

  @Bean
  @ConditionalOnMissingBean(UserMailService.class)
  public UserMailService userMailService(MailSender mailSender, ActionTokenService actionTokenService) {
    return new UserMailServiceImpl(mailSender, actionTokenService);
  }

  @Bean
  @ConditionalOnMissingBean(UserService.class)
  public UserService userService(UserDao userDao, UserDTOMapper userDTOMapper, PasswordEncoder passwordEncoder, UserMailService userMailService, ApplicationEventPublisher eventPublisher,
                                 @Value("classpath:/images/avatar.jpeg") Resource defaultAvatar) {
    return new UserServiceImpl(userDao, userDTOMapper, eventPublisher, passwordEncoder, userMailService, defaultAvatar);
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
  public IndexationEngineImpl<UserDTO> userIndexationEngine(Client client,
                                                            UserService userService,
                                                            BulkProcessor bulkProcessor,
                                                            ObjectMapper objectMapper,
                                                            @Value("classpath:/elasticsearch/users.json") Resource resource) {
    return new IndexationEngineImpl<>(UserDTO.class, client, resource, "users", u -> "user", userService, bulkProcessor, objectMapper);
  }

  @Bean
  public SearchEngineImpl<UserDTO> userSearchEngine(Client client, BulkProcessor bulkProcessor, ObjectMapper objectMapper) {
    return new SearchEngineImpl<>(UserDTO.class, client, Lists.newArrayList("identifier", "email", "firstname", "lastname", "company", "function"), "users", objectMapper);
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
  public SimpleTriggerFactoryBean userScheduledIndexationTrigger(@Qualifier("userIndexationFullJob") JobDetail userIndexationFullJob) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("User re-indexation");
    factoryBean.setDescription("Periodic re-indexation of all users of the application");
    factoryBean.setJobDetail(userIndexationFullJob);
    factoryBean.setStartDelay((long)30*1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }
}
