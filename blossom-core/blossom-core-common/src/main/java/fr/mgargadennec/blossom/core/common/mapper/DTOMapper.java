package fr.mgargadennec.blossom.core.common.mapper;

import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface DTOMapper<E extends AbstractEntity, D extends AbstractDTO> {
  D mapEntity(E entity);

  List<D> mapEntities(Collection<E> entities);

  Page<D> mapEntitiesPage(Page<E> entities);

  E mapDto(D dto);

  List<E> mapDtos(Collection<D> dtos);

  Page<E> mapDtosPage(Page<D> dtos);
}
