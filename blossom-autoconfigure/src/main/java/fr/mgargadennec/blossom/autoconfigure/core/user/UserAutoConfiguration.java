package fr.mgargadennec.blossom.autoconfigure.core.user;

import fr.mgargadennec.blossom.core.common.PredicateProvider;
import fr.mgargadennec.blossom.core.user.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnClass(User.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EntityScan(basePackageClasses = User.class)
public class UserAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(UserService.class)
  public UserService userService(UserDao userDao, UserDTOMapper userDTOMapper, ApplicationEventPublisher eventPublisher) {
    return new UserServiceImpl(userDao, userDTOMapper, eventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean(UserDao.class)
  public UserDao userDao(UserRepository userRepository) {
    return new UserDaoImpl(userRepository, userPredicateProvider());
  }

  @Bean
  @ConditionalOnMissingBean(UserDTOMapper.class)
  public UserDTOMapper userDTOMapper() {
    return new UserDTOMapper();
  }

  @Bean
  public PredicateProvider userPredicateProvider() {
    return () -> null;
  }
}
