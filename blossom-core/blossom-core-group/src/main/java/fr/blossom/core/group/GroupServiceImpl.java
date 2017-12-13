package fr.blossom.core.group;

import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.event.CreatedEvent;
import fr.blossom.core.common.event.UpdatedEvent;
import fr.blossom.core.common.mapper.DTOMapper;
import fr.blossom.core.common.service.AssociationServicePlugin;
import fr.blossom.core.common.service.GenericCrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class GroupServiceImpl extends GenericCrudServiceImpl<GroupDTO, Group> implements
  GroupService {

  private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

  public GroupServiceImpl(GroupDao dao, DTOMapper<Group, GroupDTO> mapper,
    ApplicationEventPublisher publisher,
    PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationRegistry) {
    super(dao, mapper, publisher, associationRegistry);
  }

  @Override
  @Transactional
  public GroupDTO create(GroupCreateForm groupCreateForm) throws Exception {

    Group groupToCreate = new Group();
    groupToCreate.setName(groupCreateForm.getName());
    groupToCreate.setDescription(groupCreateForm.getDescription());

    GroupDTO savedgroup = this.mapper.mapEntity(this.crudDao.create(groupToCreate));

    this.publisher.publishEvent(new CreatedEvent<GroupDTO>(this, savedgroup));

    return savedgroup;
  }

  @Override
  @Transactional
  public GroupDTO update(Long groupId, GroupUpdateForm groupUpdateForm) {

    Group groupToUpdate = new Group();
    groupToUpdate.setName(groupUpdateForm.getName());
    groupToUpdate.setDescription(groupUpdateForm.getDescription());

    GroupDTO savedgroup = this.mapper.mapEntity(this.crudDao.update(groupId, groupToUpdate));

    this.publisher.publishEvent(new UpdatedEvent<GroupDTO>(this, savedgroup));

    return savedgroup;
  }

}
