package com.blossom_project.core.user;

import com.blossom_project.core.common.mapper.AbstractDTOMapper;

public class UserDTOMapper extends AbstractDTOMapper<User, UserDTO> {

  @Override public UserDTO mapEntity(User entity) {
    if (entity == null) {
      return null;
    }

    UserDTO dto = new UserDTO();
    mapEntityCommonFields(dto, entity);
    dto.setIdentifier(entity.getIdentifier());
    dto.setPasswordHash(entity.getPasswordHash());
    dto.setDescription(entity.getDescription());
    dto.setActivated(entity.isActivated());
    dto.setLastConnection(entity.getLastConnection());
    dto.setCivility(entity.getCivility());
    dto.setFirstname(entity.getFirstname());
    dto.setLastname(entity.getLastname());
    dto.setEmail(entity.getEmail());
    dto.setPhone(entity.getPhone());
    dto.setCompany(entity.getCompany());
    dto.setFunction(entity.getFunction());
    dto.setLocale(entity.getLocale());

    return dto;
  }

  @Override public User mapDto(UserDTO dto) {
    if (dto == null) {
      return null;
    }

    User entity = new User();
    mapDtoCommonFields(entity, dto);
    entity.setIdentifier(dto.getIdentifier());
    entity.setPasswordHash(dto.getPasswordHash());
    entity.setDescription(dto.getDescription());
    entity.setActivated(dto.isActivated());
    entity.setLastConnection(dto.getLastConnection());
    entity.setCivility(dto.getCivility());
    entity.setFirstname(dto.getFirstname());
    entity.setLastname(dto.getLastname());
    entity.setEmail(dto.getEmail());
    entity.setPhone(dto.getPhone());
    entity.setCompany(dto.getCompany());
    entity.setFunction(dto.getFunction());
    entity.setLocale(dto.getLocale());

    return entity;
  }
}
