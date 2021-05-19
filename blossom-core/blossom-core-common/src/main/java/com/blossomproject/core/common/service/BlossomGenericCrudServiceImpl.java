package com.blossomproject.core.common.service;

import com.blossomproject.core.common.dao.CrudDao;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.entity.AbstractEntity;
import com.blossomproject.core.common.mapper.DTOMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;

import javax.transaction.Transactional;
import java.util.Collection;

public abstract class BlossomGenericCrudServiceImpl<DTO extends AbstractDTO, ENTITY extends AbstractEntity>
        extends GenericCrudServiceImpl<DTO, ENTITY>
        implements BlossomGenericCrudService<DTO> {

    protected BlossomGenericCrudServiceImpl(
            CrudDao<ENTITY> dao,
            DTOMapper<ENTITY, DTO> mapper,
            ApplicationEventPublisher publisher,
            PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationRegistry) {
        super(dao, mapper, publisher, associationRegistry);
    }

    @Transactional
    @Override
    public void delete(Collection<DTO> entities) {
        entities.forEach(entity -> super.delete(entity));
    }

    @Transactional
    @Override
    public void delete(Collection<DTO> entities, boolean forceAssociationDeletion) {
        entities.forEach(entity -> super.delete(entity, forceAssociationDeletion));
    }

}
