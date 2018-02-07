package com.blossom_project.core.common.service;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.blossom_project.core.common.dao.CrudDao;
import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.entity.AbstractEntity;
import com.blossom_project.core.common.event.BeforeDeletedEvent;
import com.blossom_project.core.common.event.CreatedEvent;
import com.blossom_project.core.common.event.DeletedEvent;
import com.blossom_project.core.common.event.UpdatedEvent;
import com.blossom_project.core.common.mapper.DTOMapper;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericCrudServiceImpl<DTO extends AbstractDTO, ENTITY extends AbstractEntity> extends
  GenericReadOnlyServiceImpl<DTO, ENTITY> implements CrudService<DTO> {

  private final static Logger logger = LoggerFactory.getLogger(GenericAssociationServiceImpl.class);
  private final TypeToken<DTO> dtoType = new TypeToken<DTO>(getClass()) {
  };

  protected final CrudDao<ENTITY> crudDao;
  protected final ApplicationEventPublisher publisher;
  protected final PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationRegistry;

  protected GenericCrudServiceImpl(CrudDao<ENTITY> dao, DTOMapper<ENTITY, DTO> mapper,
    ApplicationEventPublisher publisher,
    PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationRegistry) {
    super(dao, mapper);
    this.crudDao = dao;
    this.publisher = publisher;
    this.associationRegistry = associationRegistry;
  }

  @Override
  @Transactional
  public DTO create(DTO toCreate) {
    ENTITY entity = this.mapper.mapDto(toCreate);
    DTO dto = this.mapper.mapEntity(this.crudDao.create(entity));
    this.publisher.publishEvent(new CreatedEvent<DTO>(this, dto));
    return dto;
  }

  @Override
  public Map<Class<? extends AbstractDTO>, Long> associations(DTO dto) {
    Map<Class<? extends AbstractDTO>, Long> associations = Maps.newHashMap();
    if (associationRegistry.hasPluginFor(dto.getClass())) {
      List<AssociationServicePlugin> plugins = associationRegistry.getPluginsFor(dto.getClass());
      for (AssociationServicePlugin plugin : plugins) {
        Class<? extends AbstractDTO> aClass = plugin.getAClass();
        Class<? extends AbstractDTO> bClass = plugin.getBClass();
        Class<? extends AbstractDTO> clazz;
        if (aClass.isAssignableFrom(dto.getClass())) {
          clazz = bClass;
        } else {
          clazz = aClass;
        }

        int size = plugin.getAssociations(dto).size();
        if(size>0){
          associations.put(clazz, Long.valueOf(size));
        }
      }
    }

    return associations;
  }

  @Override
  @Transactional
  public Optional<Map<Class<? extends AbstractDTO>, Long>> delete(DTO toDelete) {
    return this.delete(toDelete, false);
  }

  @Override
  @Transactional
  public Optional<Map<Class<? extends AbstractDTO>, Long>> delete(DTO toDelete,
    boolean forceAssociationDeletion) {
    Map<Class<? extends AbstractDTO>, Long> associations = this.associations(toDelete);

    if (associations.isEmpty() || forceAssociationDeletion) {
      this.publisher.publishEvent(new BeforeDeletedEvent<DTO>(this, toDelete));
      if (associationRegistry.hasPluginFor(toDelete.getClass())) {
        List<AssociationServicePlugin> plugins = associationRegistry.getPluginsFor(toDelete.getClass());
        for (AssociationServicePlugin plugin : plugins) {
          plugin.dissociateAll(toDelete);
        }
      }
      this.crudDao.delete(this.mapper.mapDto(toDelete));
      this.publisher.publishEvent(new DeletedEvent<DTO>(this, toDelete));
      return Optional.empty();
    } else {
      return Optional.of(associations);
    }
  }

  @Override
  @Transactional
  public DTO update(long id, DTO toUpdate) {

    ENTITY modifiedEntity = this.mapper.mapDto(toUpdate);

    ENTITY entity = this.crudDao.update(id, modifiedEntity);

    DTO dto = this.mapper.mapEntity(entity);

    this.publisher.publishEvent(new UpdatedEvent<DTO>(this, dto));

    return dto;
  }

  @Override
  @Transactional
  public List<DTO> create(Collection<DTO> toCreates) {
    List<DTO> dtos = this.mapper.mapEntities(this.crudDao.create(this.mapper.mapDtos(toCreates)));
    dtos.forEach(dto -> this.publisher.publishEvent(new CreatedEvent<DTO>(this, dto)));
    return dtos;
  }

  @Override
  @Transactional
  public List<DTO> update(Collection<DTO> toUpdates) {
    Map<Long, ENTITY> toUpdatesEntities = this.mapper.mapDtos(toUpdates).stream()
      .collect(Collectors.toMap(entity -> entity.getId(), Function.identity()));

    List<DTO> dtos = this.mapper.mapEntities(this.crudDao.update(toUpdatesEntities));
    dtos.forEach(dto -> this.publisher.publishEvent(new UpdatedEvent<DTO>(this, dto)));
    return dtos;
  }
}
