package fr.mgargadennec.blossom.core.group;

import fr.mgargadennec.blossom.core.common.mapper.DTOMapper;
import fr.mgargadennec.blossom.core.common.service.GenericCrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class GroupServiceImpl extends GenericCrudServiceImpl<GroupDTO, Group> implements GroupService {
    private final static Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    public GroupServiceImpl(GroupDao dao, DTOMapper<Group, GroupDTO> mapper, ApplicationEventPublisher publisher) {
        super(dao, mapper, publisher);
    }

}
