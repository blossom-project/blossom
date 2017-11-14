package fr.blossom.core.group;

import fr.blossom.core.common.service.CrudService;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface GroupService extends CrudService<GroupDTO> {

  GroupDTO create(GroupCreateForm groupCreateForm) throws Exception;

  GroupDTO update(Long groupId, GroupUpdateForm groupUpdateForm) throws Exception;
}
