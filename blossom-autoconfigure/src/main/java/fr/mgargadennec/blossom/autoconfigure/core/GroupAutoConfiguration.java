package fr.mgargadennec.blossom.autoconfigure.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import fr.mgargadennec.blossom.core.common.search.IndexationEngineImpl;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.group.*;
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

import java.io.IOException;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Configuration
@ConditionalOnClass(Group.class)
@AutoConfigureAfter(UserAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = GroupRepository.class)
@EntityScan(basePackageClasses = Group.class)
public class GroupAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(GroupService.class)
    public GroupService groupService(GroupDao groupDao, GroupDTOMapper groupDTOMapper, ApplicationEventPublisher eventPublisher) {
        return new GroupServiceImpl(groupDao, groupDTOMapper, eventPublisher);
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
    public IndexationEngineImpl<GroupDTO> groupIndexationEngine(Client client,
                                                                GroupService groupService,
                                                                BulkProcessor bulkProcessor,
                                                                ObjectMapper objectMapper,
                                                                @Value("classpath:/elasticsearch/groups.json") Resource resource) throws IOException {
        return new IndexationEngineImpl<>(GroupDTO.class, client, resource, "groups", u -> "group", groupService, bulkProcessor, objectMapper);
    }

    @Bean
    public SearchEngineImpl<GroupDTO> groupSearchEngine(Client client, BulkProcessor bulkProcessor, ObjectMapper objectMapper) {
        return new SearchEngineImpl<>(GroupDTO.class, client, Lists.newArrayList("name", "description"), "groups", objectMapper);
    }


    @Bean
    @Qualifier("groupIndexationFullJob")
    public JobDetailFactoryBean groupIndexationFullJob() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(GroupIndexationJob.class);
        factoryBean.setGroup("Indexation");
        factoryBean.setName("Groups Indexation Job");
        factoryBean.setDescription("Groups Periodic Full indexation Job");
        factoryBean.setDurability(true);
        return factoryBean;
    }

    @Bean
    @Qualifier("groupScheduledIndexationTrigger")
    public SimpleTriggerFactoryBean groupScheduledIndexationTrigger(@Qualifier("groupIndexationFullJob") JobDetail groupIndexationFullJob) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName("Group re-indexation");
        factoryBean.setDescription("Periodic re-indexation of all groups of the application");
        factoryBean.setJobDetail(groupIndexationFullJob);
        factoryBean.setStartDelay((long) 30 * 1000);
        factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }

}
