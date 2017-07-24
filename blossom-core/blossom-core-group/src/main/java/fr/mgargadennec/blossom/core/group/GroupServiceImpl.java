package fr.mgargadennec.blossom.core.group;

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
public class GroupServiceImpl extends GenericCrudServiceImpl<GroupDTO, Group> implements GroupService {
  private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

  public GroupServiceImpl(GroupDao dao, DTOMapper<Group, GroupDTO> mapper, ApplicationEventPublisher publisher) {
    super(dao, mapper, publisher);
  }

  @Override
  public GroupDTO create(GroupCreateForm groupCreateForm) throws Exception {

    Group groupToCreate = new Group();
    groupToCreate.setName(groupCreateForm.getName());
    groupToCreate.setDescription(groupCreateForm.getDescription());

    GroupDTO savedgroup = this.mapper.mapEntity(this.dao.create(groupToCreate));

    this.publisher.publishEvent(new CreatedEvent<GroupDTO>(this, savedgroup));

    return savedgroup;
  }

  @Override
  public GroupDTO update(Long groupId, GroupUpdateForm groupUpdateForm) throws Exception {

    Group groupToUpdate = new Group();
    groupToUpdate.setName(groupUpdateForm.getName());
    groupToUpdate.setDescription(groupUpdateForm.getDescription());

    GroupDTO savedgroup = this.mapper.mapEntity(this.dao.update(groupId, groupToUpdate));

    this.publisher.publishEvent(new UpdatedEvent<GroupDTO>(this, savedgroup));

    return savedgroup;
  }

}
