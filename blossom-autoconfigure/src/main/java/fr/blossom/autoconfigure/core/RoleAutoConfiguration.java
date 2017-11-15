package fr.blossom.autoconfigure.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import fr.blossom.core.common.PluginConstants;
import fr.blossom.core.common.search.IndexationEngineConfiguration;
import fr.blossom.core.common.search.IndexationEngineImpl;
import fr.blossom.core.common.search.SearchEngineConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.common.search.SummaryDTO;
import fr.blossom.core.common.search.SummaryDTO.SummaryDTOBuilder;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.role.Role;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleDTOMapper;
import fr.blossom.core.role.RoleDao;
import fr.blossom.core.role.RoleDaoImpl;
import fr.blossom.core.role.RoleIndexationJob;
import fr.blossom.core.role.RoleRepository;
import fr.blossom.core.role.RoleService;
import fr.blossom.core.role.RoleServiceImpl;
import fr.blossom.core.user.UserDTO;
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

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnClass(Role.class)
@AutoConfigureAfter(GroupAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = RoleRepository.class)
@EntityScan(basePackageClasses = Role.class)
public class RoleAutoConfiguration {

  @Qualifier(PluginConstants.PLUGIN_PRIVILEGES)
  @Autowired
  private PluginRegistry<Privilege, String> privilegesRegistry;

  @Bean
  @ConditionalOnMissingBean(RoleService.class)
  public RoleService roleService(RoleDao roleDao, RoleDTOMapper roleDTOMapper,
    ApplicationEventPublisher eventPublisher) {
    return new RoleServiceImpl(roleDao, roleDTOMapper, privilegesRegistry, eventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean(RoleDao.class)
  public RoleDao roleDao(RoleRepository roleRepository) {
    return new RoleDaoImpl(roleRepository);
  }

  @Bean
  @ConditionalOnMissingBean(RoleDTOMapper.class)
  public RoleDTOMapper roleDTOMapper() {
    return new RoleDTOMapper();
  }


  @Bean
  public IndexationEngineConfiguration<RoleDTO> roleIndexationEngineConfiguration(
    @Value("classpath:/elasticsearch/roles.json") Resource resource) {
    return new IndexationEngineConfiguration<RoleDTO>() {
      @Override
      public Class<RoleDTO> getSupportedClass() {
        return RoleDTO.class;
      }

      @Override
      public Resource getSource() {
        return resource;
      }

      @Override
      public String getAlias() {
        return "roles";
      }

      @Override
      public Function<RoleDTO, String> getTypeFunction() {
        return u -> "role";
      }

      @Override
      public Function<RoleDTO, SummaryDTO> getSummaryFunction() {
        return u -> SummaryDTOBuilder.create().id(u.getId()).type(this.getTypeFunction().apply(u)).name(u.getName())
          .description(u.getDescription()).uri("/blossom/administration/roles/" + u.getId())
          .build();
      }
    };
  }

  @Bean
  public IndexationEngineImpl<RoleDTO> roleIndexationEngine(Client client,
    RoleService roleService,
    BulkProcessor bulkProcessor,
    ObjectMapper objectMapper,
    IndexationEngineConfiguration<RoleDTO> roleIndexationEngineConfiguration) {
    return new IndexationEngineImpl<>(client, roleService, bulkProcessor, objectMapper,
      roleIndexationEngineConfiguration);
  }


  @Bean
  public SearchEngineConfiguration<RoleDTO> roleSearchEngineConfiguration() {
    return new SearchEngineConfiguration<RoleDTO>() {
      @Override
      public String getName() {
        return "menu.administration.roles";
      }

      @Override
      public Class<RoleDTO> getSupportedClass() {
        return RoleDTO.class;
      }

      @Override
      public String[] getFields() {
        return new String[]{"dto.name", "dto.description"};
      }

      @Override
      public String getAlias() {
        return "roles";
      }
    };
  }

  @Bean
  public SearchEngineImpl<RoleDTO> roleSearchEngine(Client client, ObjectMapper objectMapper, SearchEngineConfiguration<RoleDTO> roleSearchEngineConfiguration) {
    return new SearchEngineImpl<>(client, objectMapper, roleSearchEngineConfiguration);
  }


  @Bean
  @Qualifier("roleIndexationFullJob")
  public JobDetailFactoryBean roleIndexationFullJob() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(RoleIndexationJob.class);
    factoryBean.setGroup("Indexation");
    factoryBean.setName("Roles Indexation Job");
    factoryBean.setDescription("Roles full indexation Job");
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean
  @Qualifier("roleScheduledIndexationTrigger")
  public SimpleTriggerFactoryBean roleScheduledIndexationTrigger(
    @Qualifier("roleIndexationFullJob") JobDetail roleIndexationFullJob) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("Role re-indexation");
    factoryBean.setDescription("Periodic re-indexation of all roles of the application");
    factoryBean.setJobDetail(roleIndexationFullJob);
    factoryBean.setStartDelay((long) 30 * 1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(
      SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }
}
