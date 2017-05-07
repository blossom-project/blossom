package fr.mgargadennec.blossom.core.role;

import fr.mgargadennec.blossom.core.common.dao.GenericCrudDaoImpl;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class RoleDaoImpl extends GenericCrudDaoImpl<Role> implements RoleDao {
    public RoleDaoImpl(RoleRepository repository) {
        super(repository);
    }

    @Override
    protected Role updateEntity(Role originalEntity, Role modifiedEntity) {
        originalEntity.setName(modifiedEntity.getName());
        originalEntity.setDescription(modifiedEntity.getDescription());
        return originalEntity;
    }
}
