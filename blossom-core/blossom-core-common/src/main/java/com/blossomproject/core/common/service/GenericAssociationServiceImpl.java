package com.blossomproject.core.common.service;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import com.blossomproject.core.common.dao.AssociationDao;
import com.blossomproject.core.common.dto.AbstractAssociationDTO;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.entity.AbstractAssociationEntity;
import com.blossomproject.core.common.entity.AbstractEntity;
import com.blossomproject.core.common.event.AfterDissociatedEvent;
import com.blossomproject.core.common.event.AssociatedEvent;
import com.blossomproject.core.common.event.BeforeDissociatedEvent;
import com.blossomproject.core.common.mapper.DTOMapper;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericAssociationServiceImpl<
  A extends AbstractDTO, B extends AbstractDTO,
  DTO extends AbstractAssociationDTO<A, B>,
  A_ENTITY extends AbstractEntity, B_ENTITY extends AbstractEntity,
  ENTITY extends AbstractAssociationEntity<A_ENTITY, B_ENTITY>>
  implements AssociationService<A, B, DTO> {

  private final static Logger logger = LoggerFactory.getLogger(GenericAssociationServiceImpl.class);
  private final TypeToken<A> aType = new TypeToken<A>(getClass()) {
  };
  private final TypeToken<B> bType = new TypeToken<B>(getClass()) {
  };

  private final AssociationDao<A_ENTITY, B_ENTITY, ENTITY> dao;
  private final DTOMapper<ENTITY, DTO> mapper;
  private final DTOMapper<A_ENTITY, A> aMapper;
  private final DTOMapper<B_ENTITY, B> bMapper;
  private final ApplicationEventPublisher eventPublisher;

  protected GenericAssociationServiceImpl(AssociationDao<A_ENTITY, B_ENTITY, ENTITY> dao,
    DTOMapper<ENTITY, DTO> mapper, DTOMapper<A_ENTITY, A> aMapper, DTOMapper<B_ENTITY, B> bMapper,
    ApplicationEventPublisher eventPublisher) {
    Preconditions.checkNotNull(dao);
    Preconditions.checkNotNull(mapper);
    Preconditions.checkNotNull(aMapper);
    Preconditions.checkNotNull(bMapper);
    Preconditions.checkNotNull(eventPublisher);
    this.dao = dao;
    this.mapper = mapper;
    this.aMapper = aMapper;
    this.bMapper = bMapper;
    this.eventPublisher = eventPublisher;
  }

  @Override
  @Transactional
  public DTO associate(A a, B b) {
    DTO dto = this.mapper.mapEntity(this.dao.associate(aMapper.mapDto(a), bMapper.mapDto(b)));
    eventPublisher.publishEvent(new AssociatedEvent<DTO>(this, dto));
    return dto;
  }

  @Override
  @Transactional
  public void dissociate(A a, B b) {
    DTO dto = this.mapper.mapEntity(this.dao.getOne(aMapper.mapDto(a), bMapper.mapDto(b)));
    eventPublisher.publishEvent(new BeforeDissociatedEvent<>(this, dto));
    this.dao.dissociate(aMapper.mapDto(a), bMapper.mapDto(b));
    eventPublisher.publishEvent(new AfterDissociatedEvent<>(this, dto));
  }

  @Override
  @Transactional
  public void dissociateAll(AbstractDTO o) {
    if (o.getClass().isAssignableFrom(aType.getRawType())) {
      this.dissociateAllLeft((A) o);
    } else if (o.getClass().isAssignableFrom(bType.getRawType())) {
      this.dissociateAllRight((B) o);
    } else {
      throw new IllegalArgumentException(
        "Cannot dissociate all from object " + o.getId() + " of type " + o.getClass()
          .getCanonicalName());
    }
  }

  @Override
  @Transactional
  public void dissociateAllLeft(A a) {
    this.dao.getAllB(aMapper.mapDto(a)).stream().forEach(this.remove());
  }

  @Override
  @Transactional
  public void dissociateAllRight(B b) {
    this.dao.getAllA(bMapper.mapDto(b)).stream().forEach(this.remove());
  }

  @Override
  public Class<A> getAClass() {
    return (Class<A>) aType.getRawType();
  }

  @Override
  public Class<B> getBClass() {
    return (Class<B>) bType.getRawType();
  }

  private Consumer<ENTITY> remove() {
    return associationEntity -> {
      if (logger.isDebugEnabled()) {
        logger.debug("Removed association between {} ({}) and {} ({})",
          aType.getRawType().getCanonicalName(), associationEntity.getA().getId(),
          bType.getRawType().getCanonicalName(), associationEntity.getB().getId());
      }
      DTO association = mapper.mapEntity(associationEntity);
      eventPublisher.publishEvent(new BeforeDissociatedEvent<>(this, association));
      dao.dissociate(associationEntity.getA(), associationEntity.getB());
      eventPublisher.publishEvent(new AfterDissociatedEvent<>(this, association));
    };
  }

  @Override
  public List<? extends AbstractAssociationDTO> getAssociations(AbstractDTO o) {
    if (o.getClass().isAssignableFrom(aType.getRawType())) {
      return this.getAllLeft((A) o);
    } else if (o.getClass().isAssignableFrom(bType.getRawType())) {
      return this.getAllRight((B) o);
    } else {
      throw new IllegalArgumentException(
        "Cannot dissociate all from object " + o.getId() + " of type " + o.getClass()
          .getCanonicalName());
    }
  }

  @Override
  public List<DTO> getAllRight(B b) {
    return this.mapper.mapEntities(this.dao.getAllA(bMapper.mapDto(b)));
  }

  @Override
  public List<DTO> getAllLeft(A a) {
    return this.mapper.mapEntities(this.dao.getAllB(aMapper.mapDto(a)));
  }

  @Override
  public DTO getOne(long id) {
    return this.mapper.mapEntity(this.dao.getOne(id));
  }
}
