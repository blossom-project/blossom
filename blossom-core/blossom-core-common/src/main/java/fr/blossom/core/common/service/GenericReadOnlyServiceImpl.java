package fr.blossom.core.common.service;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import fr.blossom.core.common.dao.ReadOnlyDao;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.entity.AbstractEntity;
import fr.blossom.core.common.mapper.DTOMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class GenericReadOnlyServiceImpl<DTO extends AbstractDTO, ENTITY extends AbstractEntity> implements ReadOnlyService<DTO> {
  private final TypeToken<DTO> typeToken = new TypeToken<DTO>(getClass()) {
  };
  protected final ReadOnlyDao<ENTITY> dao;
  protected final DTOMapper<ENTITY, DTO> mapper;

  public GenericReadOnlyServiceImpl(ReadOnlyDao<ENTITY> dao, DTOMapper<ENTITY, DTO> mapper) {
    this.dao = dao;
    this.mapper = mapper;
  }

  @Override
  public DTO getOne(Long id) {
    Preconditions.checkNotNull(id, "The id cannot be null");
    return mapper.mapEntity(this.dao.getOne(id));
  }

  @Override
  public List<DTO> getAll(List<Long> ids) {
    return mapper.mapEntities(this.dao.getAll(ids));
  }

  @Override
  public Page<DTO> getAll(Pageable pageable) {
    return mapper.mapEntitiesPage(this.dao.getAll(pageable));
  }

  @Override
  public List<DTO> getAll() {
    return mapper.mapEntities(this.dao.getAll());
  }

  @Override
  public boolean supports(Class<? extends AbstractDTO> delimiter) {
    return delimiter.isAssignableFrom(typeToken.getRawType());
  }
}
