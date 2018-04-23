package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.IndexationEngineConfiguration;
import com.blossomproject.core.common.search.IndexationEngineImpl;
import com.blossomproject.core.common.search.SearchEngine;
import com.blossomproject.core.common.search.SearchEngineConfiguration;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SummaryDTO;
import com.blossomproject.core.common.search.SummaryDTO.SummaryDTOBuilder;
import com.blossomproject.core.common.search.facet.AggregationConverter;
import com.blossomproject.core.common.service.AssociationServicePlugin;
import com.blossomproject.core.group.Group;
import com.blossomproject.core.group.GroupDTO;
import com.blossomproject.core.group.GroupDTOMapper;
import com.blossomproject.core.group.GroupDao;
import com.blossomproject.core.group.GroupDaoImpl;
import com.blossomproject.core.group.GroupIndexationJob;
import com.blossomproject.core.group.GroupRepository;
import com.blossomproject.core.group.GroupService;
import com.blossomproject.core.group.GroupServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnClass(Group.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = GroupRepository.class)
@EntityScan(basePackageClasses = Group.class)
public class GroupAutoConfiguration {

  @Qualifier(PluginConstants.PLUGIN_ASSOCIATION_SERVICE)
  @Autowired
  private PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationServicePlugins;

  @Bean
  @ConditionalOnMissingBean(GroupService.class)
  public GroupService groupService(GroupDao groupDao, GroupDTOMapper groupDTOMapper,
    ApplicationEventPublisher eventPublisher) {
    return new GroupServiceImpl(groupDao, groupDTOMapper, eventPublisher,
      associationServicePlugins);
  }

  @Bean
  @ConditionalOnMissingBean(GroupDao.class)
  public GroupDao groupDao(GroupRepository groupRepository) {
    return new GroupDaoImpl(groupRepository);
  }

  @Bean
  @ConditionalOnMissingBean(GroupDTOMapper.class)
  public GroupDTOMapper groupDTOMapper() {
    return new GroupDTOMapper();
  }

  @Bean
  public IndexationEngineConfiguration<GroupDTO> groupIndexationEngineConfiguration(
    @Value("classpath:/elasticsearch/groups.json") Resource resource) {
    return new IndexationEngineConfiguration<GroupDTO>() {
      @Override
      public Class<GroupDTO> getSupportedClass() {
        return GroupDTO.class;
      }

      @Override
      public Resource getSource() {
        return resource;
      }

      @Override
      public String getAlias() {
        return "groups";
      }

      @Override
      public Function<GroupDTO, String> getTypeFunction() {
        return u -> "group";
      }

      @Override
      public Function<GroupDTO, SummaryDTO> getSummaryFunction() {
        return u -> SummaryDTOBuilder.create().id(u.getId()).type(this.getTypeFunction().apply(u))
          .name(u.getName()).description(u.getDescription())
          .uri("/blossom/administration/groups/" + u.getId()).build();
      }
    };
  }

  @Bean
  public IndexationEngineImpl<GroupDTO> groupIndexationEngine(Client client,
    GroupService groupService,
    BulkProcessor bulkProcessor,
    ObjectMapper objectMapper,
    IndexationEngineConfiguration<GroupDTO> groupIndexationEngineConfiguration
  ) throws IOException {
    return new IndexationEngineImpl<>(client, groupService, bulkProcessor, objectMapper,
      groupIndexationEngineConfiguration);
  }


  @Bean
  public SearchEngineConfiguration<GroupDTO> groupSearchEngineConfiguration() {
    return new SearchEngineConfiguration<GroupDTO>() {
      @Override
      public String getName() {
        return "menu.administration.groups";
      }

      @Override
      public Class<GroupDTO> getSupportedClass() {
        return GroupDTO.class;
      }

      @Override
      public String[] getFields() {
        return new String[]{"dto.name", "dto.description"};
      }

      @Override
      public String getAlias() {
        return "groups";
      }
    };
  }

  @Bean
  public SearchEngineImpl<GroupDTO> groupSearchEngine(Client client, ObjectMapper objectMapper,
    SearchEngineConfiguration<GroupDTO> groupSearchEngineConfiguration,
    @Qualifier(PluginConstants.PLUGIN_SEARCH_ENGINE_AGGREGATION_CONVERTERS)
      PluginRegistry<AggregationConverter, SearchEngine> aggregationConverters) {
    return new SearchEngineImpl<>(client, objectMapper, aggregationConverters,
      groupSearchEngineConfiguration);
  }


  @Bean
  @Qualifier("groupIndexationFullJob")
  public JobDetailFactoryBean groupIndexationFullJob() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(GroupIndexationJob.class);
    factoryBean.setGroup("Indexation");
    factoryBean.setName("Groups Indexation Job");
    factoryBean.setDescription("Groups full indexation Job");
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean
  @Qualifier("groupScheduledIndexationTrigger")
  public SimpleTriggerFactoryBean groupScheduledIndexationTrigger(
    @Qualifier("groupIndexationFullJob") JobDetail groupIndexationFullJob) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("Group re-indexation");
    factoryBean.setDescription("Periodic re-indexation of all groups of the application");
    factoryBean.setJobDetail(groupIndexationFullJob);
    factoryBean.setStartDelay((long) 30 * 1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(
      SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }

}
