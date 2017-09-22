package fr.mgargadennec.blossom.core.role;

import fr.mgargadennec.blossom.core.common.mapper.AbstractDTOMapper;

public class RoleDTOMapper extends AbstractDTOMapper<Role, RoleDTO> {

  @Override
  public RoleDTO mapEntity(Role entity) {
    if (entity == null) {
      return null;
    }

    RoleDTO dto = new RoleDTO();
    mapEntityCommonFields(dto, entity);
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setPrivileges(entity.getPrivileges());

    return dto;
  }

  @Override
  public Role mapDto(RoleDTO dto) {
    if (dto == null) {
      return null;
    }

    Role entity = new Role();
    mapDtoCommonFields(entity, dto);
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setPrivileges(dto.getPrivileges());

    return entity;
  }
}
