package fr.mgargadennec.blossom.core.common.service;

import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;

import java.util.Collection;
import java.util.List;

public interface CrudService<DTO extends AbstractDTO> extends ReadOnlyService<DTO> {

  DTO create(DTO toCreate);

  DTO update(long id, DTO toUpdate);

  void delete(DTO toDelete);

  List<DTO> create(Collection<DTO> toCreates);

  List<DTO> update(Collection<DTO> toUpdates);

}
