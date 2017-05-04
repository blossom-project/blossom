package fr.mgargadennec.blossom.core.common.service;

import com.google.common.base.Preconditions;
import fr.mgargadennec.blossom.core.common.dao.CrudDao;
import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;
import fr.mgargadennec.blossom.core.common.mapper.DTOMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class GenericReadOnlyServiceImpl<DTO extends AbstractDTO, ENTITY extends AbstractEntity> implements ReadOnlyService<DTO> {

  private final CrudDao<ENTITY> dao;
  protected final DTOMapper<ENTITY, DTO> mapper;

  GenericReadOnlyServiceImpl(CrudDao<ENTITY> dao, DTOMapper<ENTITY, DTO> mapper) {
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

}
