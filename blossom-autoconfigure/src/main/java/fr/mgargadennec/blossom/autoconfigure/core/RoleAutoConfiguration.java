package fr.mgargadennec.blossom.autoconfigure.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.core.common.search.IndexationEngineImpl;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.role.*;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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

    @Bean
    @ConditionalOnMissingBean(RoleService.class)
    public RoleService roleService(RoleDao roleDao, RoleDTOMapper roleDTOMapper, ApplicationEventPublisher eventPublisher) {
        return new RoleServiceImpl(roleDao, roleDTOMapper, eventPublisher);
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
    public IndexationEngineImpl<RoleDTO> roleIndexationEngine(Client client, RoleService roleService, BulkProcessor bulkProcessor, ObjectMapper objectMapper) {
        return new IndexationEngineImpl<>(RoleDTO.class, client, null, "roles", u -> "role", roleService, bulkProcessor, objectMapper);
    }

    @Bean
    public SearchEngineImpl<RoleDTO> roleSearchEngine(Client client, BulkProcessor bulkProcessor, ObjectMapper objectMapper) {
        return new SearchEngineImpl<>(RoleDTO.class, client, Lists.newArrayList("name", "description"), "roles", objectMapper);
    }


  @Bean
  @Qualifier("roleIndexationFullJob")
  public JobDetailFactoryBean roleIndexationFullJob() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(RoleIndexationJob.class);
    factoryBean.setGroup("Indexation");
    factoryBean.setName("Roles Indexation Job");
    factoryBean.setDescription("Roles Periodic Full indexation Job");
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean
  @Qualifier("roleScheduledIndexationTrigger")
  public SimpleTriggerFactoryBean roleScheduledIndexationTrigger(@Qualifier("roleIndexationFullJob") JobDetail roleIndexationFullJob) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("Role re-indexation");
    factoryBean.setDescription("Periodic re-indexation of all roles of the application");
    factoryBean.setJobDetail(roleIndexationFullJob);
    factoryBean.setStartDelay((long)30*1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }

}
