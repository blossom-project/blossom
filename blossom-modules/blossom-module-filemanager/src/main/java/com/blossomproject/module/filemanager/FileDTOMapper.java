package com.blossomproject.module.filemanager;

import com.blossomproject.core.common.mapper.AbstractDTOMapper;

public class FileDTOMapper extends AbstractDTOMapper<File, FileDTO> {

  @Override
  public FileDTO mapEntity(File entity) {
    if (entity == null) {
      return null;
    }

    FileDTO dto = new FileDTO();
    mapEntityCommonFields(dto, entity);
    dto.setName(entity.getName());
    dto.setContentType(entity.getContentType());
    dto.setExtension(entity.getExtension());
    dto.setSize(entity.getSize());
    dto.setTags(entity.getTags());
    dto.setHash(entity.getHash());
    dto.setHashAlgorithm(entity.getHashAlgorithm());

    return dto;
  }

  @Override
  public File mapDto(FileDTO dto) {
    if (dto == null) {
      return null;
    }

    File entity = new File();
    mapDtoCommonFields(entity, dto);
    entity.setName(dto.getName());
    entity.setContentType(dto.getContentType());
    entity.setExtension(dto.getExtension());
    entity.setSize(dto.getSize());
    entity.setTags(dto.getTags());
    entity.setHash(dto.getHash());
    entity.setHashAlgorithm(dto.getHashAlgorithm());

    return entity;
  }
}
