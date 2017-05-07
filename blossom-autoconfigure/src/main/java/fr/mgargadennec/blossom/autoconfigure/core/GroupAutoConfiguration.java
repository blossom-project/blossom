package fr.mgargadennec.blossom.autoconfigure.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.core.common.search.IndexationEngineImpl;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.group.*;
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
    public IndexationEngineImpl<GroupDTO> groupIndexationEngine(Client client, GroupService groupService, BulkProcessor bulkProcessor, ObjectMapper objectMapper) {
        return new IndexationEngineImpl<>(GroupDTO.class, client, null, "groups", u -> "group", groupService, bulkProcessor, objectMapper);
    }

    @Bean
    public SearchEngineImpl<GroupDTO> groupSearchEngine(Client client, BulkProcessor bulkProcessor, ObjectMapper objectMapper) {
        return new SearchEngineImpl<>(GroupDTO.class, client, Lists.newArrayList("name", "description"), "groups", objectMapper);
    }

}
