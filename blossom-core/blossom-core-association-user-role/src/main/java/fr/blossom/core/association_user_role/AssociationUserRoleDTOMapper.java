package fr.blossom.core.association_user_role;

import fr.blossom.core.common.mapper.AbstractDTOMapper;
import fr.blossom.core.role.RoleDTOMapper;
import fr.blossom.core.user.UserDTOMapper;

public class AssociationUserRoleDTOMapper extends AbstractDTOMapper<AssociationUserRole, AssociationUserRoleDTO> {
    private final UserDTOMapper userMapper;
    private final RoleDTOMapper roleMapper;

    public AssociationUserRoleDTOMapper(UserDTOMapper userMapper, RoleDTOMapper groupMapper) {
        this.userMapper = userMapper;
        this.roleMapper = groupMapper;
    }

    @Override
    public AssociationUserRoleDTO mapEntity(AssociationUserRole entity) {
        if (entity == null) {
            return null;
        }

        AssociationUserRoleDTO dto = new AssociationUserRoleDTO();
        mapEntityCommonFields(dto, entity);
        dto.setA(userMapper.mapEntity(entity.getA()));
        dto.setB(roleMapper.mapEntity(entity.getB()));

        return dto;
    }

    @Override
    public AssociationUserRole mapDto(AssociationUserRoleDTO dto) {
        if (dto == null) {
            return null;
        }

        AssociationUserRole entity = new AssociationUserRole();
        mapDtoCommonFields(entity, dto);
        entity.setA(userMapper.mapDto(dto.getA()));
        entity.setB(roleMapper.mapDto(dto.getB()));

        return entity;
    }
}
