package com.blossomproject.core.common.service;

import com.blossomproject.core.common.dto.AbstractDTO;

import java.util.Collection;

public interface BlossomGenericCrudService<DTO extends AbstractDTO> extends CrudService<DTO> {

    void delete(Collection<DTO> entities);

    void delete(Collection<DTO> entities, boolean forceAssociationDeletion);


}
