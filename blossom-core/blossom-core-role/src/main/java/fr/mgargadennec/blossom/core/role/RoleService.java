package fr.mgargadennec.blossom.core.role;

import fr.mgargadennec.blossom.core.common.service.CrudService;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface RoleService extends CrudService<RoleDTO> {

  RoleDTO create(RoleCreateForm userCreateForm) throws Exception;

  RoleDTO update(Long roleId, RoleUpdateForm roleUpdateForm) throws Exception;
}
