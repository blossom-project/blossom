package fr.blossom.core.common.mapper;

import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.entity.AbstractEntity;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

/**
 * Mapper that transforms a given {@code AbstractEntity} into the corresponding {@code AbstractDTO}?
 *
 * @param <ENTITY> the entity class
 * @param <DTO> the dto class
 * @author Maël Gargadennnec
 */
public interface DTOMapper<ENTITY extends AbstractEntity, DTO extends AbstractDTO> extends
  MapperPlugin {

  /**
   *
   * @param entity the entity to map/
   * @return the mapped dto.
   */
  DTO mapEntity(ENTITY entity);

  ENTITY mapDto(DTO dto);


  default List<DTO> mapEntities(Collection<ENTITY> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream().map(this::mapEntity).collect(Collectors.toList());
  }

  default Page<DTO> mapEntitiesPage(Page<ENTITY> entities) {
    if (entities == null) {
      return null;
    }
    return entities.map(this::mapEntity);
  }

  default List<ENTITY> mapDtos(Collection<DTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream().map(this::mapDto).collect(Collectors.toList());
  }

  default Page<ENTITY> mapDtosPage(Page<DTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.map(this::mapDto);
  }
}
