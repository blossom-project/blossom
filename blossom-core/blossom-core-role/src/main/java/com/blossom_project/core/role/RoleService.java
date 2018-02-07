package com.blossom_project.core.role;

import com.blossom_project.core.common.service.CrudService;
import com.blossom_project.core.common.utils.privilege.Privilege;
import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface RoleService extends CrudService<RoleDTO> {

  RoleDTO create(RoleCreateForm roleCreateForm) throws Exception;

  RoleDTO update(Long roleId, RoleUpdateForm roleUpdateForm);

  List<Privilege> getAvailablePrivileges();
}
