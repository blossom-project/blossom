package com.blossom_project.autoconfigure.ui.api.administration;

import com.blossom_project.autoconfigure.ui.api.ApiInterfaceAutoConfiguration;
import com.blossom_project.autoconfigure.ui.common.privileges.GroupPrivilegesConfiguration;
import com.blossom_project.core.common.search.SearchEngineImpl;
import com.blossom_project.core.group.GroupDTO;
import com.blossom_project.core.group.GroupService;
import com.blossom_project.ui.api.administration.GroupsApiController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass({GroupService.class, GroupsApiController.class})
@ConditionalOnBean(GroupService.class)
@AutoConfigureAfter(ApiInterfaceAutoConfiguration.class)
@Import(GroupPrivilegesConfiguration.class)
public class ApiAdministrationGroupAutoConfiguration {

  @Bean
  public GroupsApiController groupsApiController(GroupService groupService,
    SearchEngineImpl<GroupDTO> searchEngine) {
    return new GroupsApiController(groupService, searchEngine);
  }

}
