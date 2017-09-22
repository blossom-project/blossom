package fr.mgargadennec.blossom.core.role;

import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.core.common.event.CreatedEvent;
import fr.mgargadennec.blossom.core.common.event.UpdatedEvent;
import fr.mgargadennec.blossom.core.common.mapper.DTOMapper;
import fr.mgargadennec.blossom.core.common.service.GenericCrudServiceImpl;
import fr.mgargadennec.blossom.core.common.utils.privilege.PrivilegePlugin;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

  private final PluginRegistry<PrivilegePlugin, String> privilegesRegistry;

  public RoleServiceImpl(RoleDao dao, DTOMapper<Role, RoleDTO> mapper,
    PluginRegistry<PrivilegePlugin, String> privilegesRegistry,
    ApplicationEventPublisher publisher) {
    super(dao, mapper, publisher);
    this.privilegesRegistry = privilegesRegistry;
  }

  @Override
  @Transactional
  public RoleDTO create(RoleCreateForm roleCreateForm) throws Exception {

    Role roleToCreate = new Role();
    roleToCreate.setName(roleCreateForm.getName());
    roleToCreate.setDescription(roleCreateForm.getDescription());
    roleToCreate.setPrivileges(Lists.newArrayList());

    RoleDTO savedRole = this.mapper.mapEntity(this.dao.create(roleToCreate));

    this.publisher.publishEvent(new CreatedEvent<RoleDTO>(this, savedRole));

    return savedRole;
  }

  @Override
  @Transactional
  public RoleDTO update(Long roleId, RoleUpdateForm roleUpdateForm) throws Exception {

    Role roleToUpdate = new Role();
    roleToUpdate.setName(roleUpdateForm.getName());
    roleToUpdate.setDescription(roleUpdateForm.getDescription());

    RoleDTO savedRole = this.mapper.mapEntity(this.dao.update(roleId, roleToUpdate));

    this.publisher.publishEvent(new UpdatedEvent<RoleDTO>(this, savedRole));

    return savedRole;
  }

  @Override
  public Map<String, List<PrivilegePlugin>> getAvailablePrivileges() {
    return privilegesRegistry.getPlugins().stream().collect(
      Collectors.groupingBy(plugin -> plugin.namespace()));
  }
}
