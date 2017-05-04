package fr.mgargadennec.blossom.core.user;

import fr.mgargadennec.blossom.core.common.mapper.AbstractDTOMapper;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class UserDTOMapper extends AbstractDTOMapper<User, UserDTO> {
  @Override
  public UserDTO mapEntity(User entity) {
    UserDTO dto = new UserDTO();
    mapEntityCommonFields(dto, entity);
    return dto;
  }

  @Override
  public User mapDto(UserDTO dto) {
    User entity = new User();
    mapDtoCommonFields(entity, dto);
    return entity;
  }
}
