package com.blossomproject.core.role;

import com.google.common.collect.Lists;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.event.CreatedEvent;
import com.blossomproject.core.common.event.UpdatedEvent;
import com.blossomproject.core.common.mapper.DTOMapper;
import com.blossomproject.core.common.service.AssociationServicePlugin;
import com.blossomproject.core.common.service.GenericCrudServiceImpl;
import com.blossomproject.core.common.utils.privilege.Privilege;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class RoleServiceImpl extends GenericCrudServiceImpl<RoleDTO, Role> implements RoleService {

  private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

  private final PluginRegistry<Privilege, String> privilegesRegistry;

  public RoleServiceImpl(RoleDao dao, DTOMapper<Role, RoleDTO> mapper,
    PluginRegistry<Privilege, String> privilegesRegistry,
    ApplicationEventPublisher publisher,
    PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationRegistry) {
    super(dao, mapper, publisher, associationRegistry);
    this.privilegesRegistry = privilegesRegistry;
  }

  @Override
  @Transactional
  public RoleDTO create(RoleCreateForm roleCreateForm) throws Exception {

    Role roleToCreate = new Role();
    roleToCreate.setName(roleCreateForm.getName());
    roleToCreate.setDescription(roleCreateForm.getDescription());
    roleToCreate.setPrivileges(Lists.newArrayList());

    RoleDTO savedRole = this.mapper.mapEntity(this.crudDao.create(roleToCreate));

    this.publisher.publishEvent(new CreatedEvent<RoleDTO>(this, savedRole));

    return savedRole;
  }

  @Override
  @Transactional
  public RoleDTO update(Long roleId, RoleUpdateForm roleUpdateForm) {

    Role roleToUpdate = new Role();
    roleToUpdate.setName(roleUpdateForm.getName());
    roleToUpdate.setDescription(roleUpdateForm.getDescription());

    RoleDTO savedRole = this.mapper.mapEntity(this.crudDao.update(roleId, roleToUpdate));

    this.publisher.publishEvent(new UpdatedEvent<RoleDTO>(this, savedRole));

    return savedRole;
  }

  @Override
  public List<Privilege> getAvailablePrivileges() {
    return privilegesRegistry.getPlugins();
  }
}
