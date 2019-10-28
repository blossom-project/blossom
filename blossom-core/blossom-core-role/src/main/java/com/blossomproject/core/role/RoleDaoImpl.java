package com.blossomproject.core.role;

import com.blossomproject.core.common.dao.BlossomGenericCrudDaoImpl;
import com.blossomproject.core.common.dao.GenericCrudDaoImpl;
import org.mapstruct.factory.Mappers;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class RoleDaoImpl extends BlossomGenericCrudDaoImpl<Role> implements RoleDao {
    public RoleDaoImpl(RoleRepository repository) {
        super(repository, Mappers.getMapper(RoleEntityMapper.class));
    }

    @Override
    protected Role updateEntity(Role originalEntity, Role modifiedEntity) {
        originalEntity.setName(modifiedEntity.getName());
        originalEntity.setDescription(modifiedEntity.getDescription());
        originalEntity.getPrivileges().clear();
        originalEntity.getPrivileges().addAll(modifiedEntity.getPrivileges());
        return originalEntity;
    }
}
