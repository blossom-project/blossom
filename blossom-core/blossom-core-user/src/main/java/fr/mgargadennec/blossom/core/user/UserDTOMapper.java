package fr.mgargadennec.blossom.core.user;


import fr.mgargadennec.blossom.core.common.mapper.AbstractDTOMapper;

public class UserDTOMapper extends AbstractDTOMapper<User, UserDTO> {

  @Override
  public UserDTO mapEntity(User entity) {
    if (entity == null) {
      return null;
    }

    UserDTO dto = new UserDTO();
    dto.setId(entity.getId());
    dto.setIdentifier(entity.getIdentifier());
    dto.setFirstname(entity.getFirstname());
    dto.setLastname(entity.getLastname());
    dto.setDescription(entity.getDescription());
    dto.setEmail(entity.getEmail());
    dto.setActivated(entity.isActivated());
    dto.setPasswordHash(entity.getPasswordHash());
    dto.setPhone(entity.getPhone());
    dto.setFunction(entity.getFunction());
    dto.setLastConnection(entity.getLastConnection());
    dto.setDateCreation(entity.getDateCreation());
    dto.setDateModification(entity.getDateModification());
    dto.setUserCreation(entity.getUserCreation());
    dto.setUserModification(entity.getUserModification());

    return dto;
  }

  @Override
  public User mapDto(UserDTO dto) {
    if (dto == null) {
      return null;
    }

    User entity = new User();
    entity.setId(dto.getId());
    entity.setIdentifier(dto.getIdentifier());
    entity.setFirstname(dto.getFirstname());
    entity.setLastname(dto.getLastname());
    entity.setDescription(dto.getDescription());
    entity.setEmail(dto.getEmail());
    entity.setActivated(dto.isActivated());
    entity.setPasswordHash(dto.getPasswordHash());
    entity.setPhone(dto.getPhone());
    entity.setFunction(dto.getFunction());
    entity.setLastConnection(dto.getLastConnection());
    entity.setDateCreation(dto.getDateCreation());
    entity.setDateModification(dto.getDateModification());
    entity.setUserCreation(dto.getUserCreation());
    entity.setUserModification(dto.getUserModification());

    return entity;
  }
}
