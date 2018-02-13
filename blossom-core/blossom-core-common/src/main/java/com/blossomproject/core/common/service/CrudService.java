package com.blossomproject.core.common.service;

import com.blossomproject.core.common.dto.AbstractDTO;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrudService<DTO extends AbstractDTO> extends ReadOnlyService<DTO> {

  DTO create(DTO toCreate);

  DTO update(long id, DTO toUpdate);

  Map<Class<? extends AbstractDTO>, Long> associations(DTO toDelete);

  Optional<Map<Class<? extends AbstractDTO>, Long>> delete(DTO toDelete);

  Optional<Map<Class<? extends AbstractDTO>, Long>> delete(DTO toDelete, boolean forceAssociationDeletion);

  List<DTO> create(Collection<DTO> toCreates);

  List<DTO> update(Collection<DTO> toUpdates);

}
