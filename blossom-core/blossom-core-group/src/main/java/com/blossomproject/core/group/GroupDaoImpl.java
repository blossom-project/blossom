package com.blossomproject.core.group;

import com.blossomproject.core.common.dao.BlossomGenericCrudDaoImpl;
import org.mapstruct.factory.Mappers;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class GroupDaoImpl extends BlossomGenericCrudDaoImpl<Group> implements GroupDao {
    
    public GroupDaoImpl(GroupRepository repository) {
        super(repository, Mappers.getMapper(GroupEntityMapper.class));
    }

}
