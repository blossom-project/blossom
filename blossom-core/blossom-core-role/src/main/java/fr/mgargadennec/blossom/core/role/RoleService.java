package fr.mgargadennec.blossom.core.role;

import fr.mgargadennec.blossom.core.common.service.CrudService;
import fr.mgargadennec.blossom.core.common.utils.privilege.PrivilegePlugin;
import java.util.List;
import java.util.Map;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface RoleService extends CrudService<RoleDTO> {

  RoleDTO create(RoleCreateForm roleCreateForm) throws Exception;

  RoleDTO update(Long roleId, RoleUpdateForm roleUpdateForm) throws Exception;

  Map<String, List<PrivilegePlugin>> getAvailablePrivileges();
}
