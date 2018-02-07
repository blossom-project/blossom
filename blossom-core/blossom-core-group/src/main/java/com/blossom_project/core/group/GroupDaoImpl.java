package com.blossom_project.core.group;

import com.blossom_project.core.common.dao.GenericCrudDaoImpl;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class GroupDaoImpl extends GenericCrudDaoImpl<Group> implements GroupDao {
    public GroupDaoImpl(GroupRepository repository) {
        super(repository);
    }

    @Override
    protected Group updateEntity(Group originalEntity, Group modifiedEntity) {
        originalEntity.setName(modifiedEntity.getName());
        originalEntity.setDescription(modifiedEntity.getDescription());
        return originalEntity;
    }
}
