package fr.mgargadennec.blossom.core.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import fr.mgargadennec.blossom.core.common.event.CreatedEvent;
import fr.mgargadennec.blossom.core.common.event.UpdatedEvent;
import fr.mgargadennec.blossom.core.common.mapper.DTOMapper;
import fr.mgargadennec.blossom.core.common.service.GenericCrudServiceImpl;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class RoleServiceImpl extends GenericCrudServiceImpl<RoleDTO, Role> implements RoleService {
  private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

  public RoleServiceImpl(RoleDao dao, DTOMapper<Role, RoleDTO> mapper, ApplicationEventPublisher publisher) {
    super(dao, mapper, publisher);
  }

  @Override
  public RoleDTO create(RoleCreateForm roleCreateForm) throws Exception {

    Role roleToCreate = new Role();
    roleToCreate.setName(roleCreateForm.getName());
    roleToCreate.setDescription(roleCreateForm.getDescription());

    RoleDTO savedRole = this.mapper.mapEntity(this.dao.create(roleToCreate));

    this.publisher.publishEvent(new CreatedEvent<RoleDTO>(this, savedRole));

    return savedRole;
  }

  @Override
  public RoleDTO update(Long roleId, RoleUpdateForm roleUpdateForm) throws Exception {

    Role roleToUpdate = new Role();
    roleToUpdate.setName(roleUpdateForm.getName());
    roleToUpdate.setDescription(roleUpdateForm.getDescription());

    RoleDTO savedRole = this.mapper.mapEntity(this.dao.update(roleId, roleToUpdate));

    this.publisher.publishEvent(new UpdatedEvent<RoleDTO>(this, savedRole));

    return savedRole;
  }

}
