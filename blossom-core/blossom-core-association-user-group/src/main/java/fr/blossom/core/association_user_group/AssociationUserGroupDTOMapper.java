package fr.blossom.core.association_user_group;

import com.google.common.base.Preconditions;
import fr.blossom.core.common.mapper.AbstractDTOMapper;
import fr.blossom.core.group.GroupDTOMapper;
import fr.blossom.core.user.UserDTOMapper;

public class AssociationUserGroupDTOMapper extends
  AbstractDTOMapper<AssociationUserGroup, AssociationUserGroupDTO> {

  private final UserDTOMapper userMapper;
  private final GroupDTOMapper groupMapper;

  public AssociationUserGroupDTOMapper(UserDTOMapper userMapper, GroupDTOMapper groupMapper) {
    Preconditions.checkNotNull(userMapper);
    Preconditions.checkNotNull(groupMapper);
    this.userMapper = userMapper;
    this.groupMapper = groupMapper;
  }

  @Override
  public AssociationUserGroupDTO mapEntity(AssociationUserGroup entity) {
    if (entity == null) {
      return null;
    }

    AssociationUserGroupDTO dto = new AssociationUserGroupDTO();
    mapEntityCommonFields(dto, entity);
    dto.setA(userMapper.mapEntity(entity.getA()));
    dto.setB(groupMapper.mapEntity(entity.getB()));

    return dto;
  }

  @Override
  public AssociationUserGroup mapDto(AssociationUserGroupDTO dto) {
    if (dto == null) {
      return null;
    }

    AssociationUserGroup entity = new AssociationUserGroup();
    mapDtoCommonFields(entity, dto);
    entity.setA(userMapper.mapDto(dto.getA()));
    entity.setB(groupMapper.mapDto(dto.getB()));

    return entity;
  }
}
