package fr.mgargadennec.blossom.autoconfigure.core.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.common.search.IndexationEngineImpl;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.common.utils.action_token.ActionTokenService;
import fr.mgargadennec.blossom.core.common.utils.mail.MailSender;
import fr.mgargadennec.blossom.core.user.*;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
  public UserService userService(UserDao userDao, UserDTOMapper userDTOMapper, PasswordEncoder passwordEncoder, UserMailService userMailService, ApplicationEventPublisher eventPublisher) {
    return new UserServiceImpl(userDao, userDTOMapper, eventPublisher, passwordEncoder, userMailService);
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
  public IndexationEngineImpl<UserDTO> userIndexationEngine(Client client, UserService userService, BulkProcessor bulkProcessor, ObjectMapper objectMapper) {
    return new IndexationEngineImpl<>(UserDTO.class, client, null, "users", u -> "user", userService, bulkProcessor, objectMapper);
  }

  @Bean
  public SearchEngineImpl<UserDTO> userSearchEngine(Client client, BulkProcessor bulkProcessor, ObjectMapper objectMapper) {
    return new SearchEngineImpl<>(UserDTO.class, client, Lists.newArrayList("firstname", "lastname", "identifier", "email"), "users", objectMapper);
  }

}
