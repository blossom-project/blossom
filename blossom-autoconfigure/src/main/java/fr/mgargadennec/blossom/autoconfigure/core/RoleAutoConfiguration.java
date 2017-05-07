package fr.mgargadennec.blossom.autoconfigure.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.autoconfigure.core.GroupAutoConfiguration;
import fr.mgargadennec.blossom.core.common.search.IndexationEngineImpl;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.role.*;
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

}
