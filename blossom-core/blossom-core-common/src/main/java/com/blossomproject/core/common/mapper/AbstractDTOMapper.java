package com.blossomproject.core.common.mapper;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.entity.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base {@code DTOMapper} that provides utility methods for mapping common fields.
 *
 * @param <ENTITY> the entity class
 * @param <DTO> the dto class
 * @author Maël Gargadennnec
 */
public abstract class AbstractDTOMapper<ENTITY extends AbstractEntity, DTO extends AbstractDTO> implements
  DTOMapper<ENTITY, DTO> {

  private final static Logger LOGGER = LoggerFactory.getLogger(AbstractDTOMapper.class);
  private final TypeToken<ENTITY> typeToken = new TypeToken<ENTITY>(getClass()) {
  };

  public AbstractDTOMapper() {
    LOGGER.info("Creating AbstractDTOMapper for entity class {}", typeToken.getRawType());
  }

  protected void mapEntityCommonFields(DTO dto, ENTITY entity) {
    Preconditions.checkArgument(dto != null);
    Preconditions.checkArgument(entity != null);
    dto.setId(entity.getId());
    dto.setCreationDate(entity.getCreationDate());
    dto.setModificationDate(entity.getModificationDate());
    dto.setCreationUser(entity.getCreationUser());
    dto.setModificationUser(entity.getModificationUser());
  }

  protected void mapDtoCommonFields(ENTITY entity, DTO dto) {
    Preconditions.checkArgument(dto != null);
    Preconditions.checkArgument(entity != null);
    entity.setId(dto.getId());
    entity.setCreationDate(dto.getCreationDate());
    entity.setModificationDate(dto.getModificationDate());
    entity.setCreationUser(dto.getCreationUser());
    entity.setModificationUser(dto.getModificationUser());
  }

  @Override
  public boolean supports(Class<? extends AbstractEntity> delimiter) {
    return delimiter.isAssignableFrom(typeToken.getRawType());
  }
}
