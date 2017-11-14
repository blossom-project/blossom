package fr.blossom.core.common.service;

import fr.blossom.core.common.dao.AssociationDao;
import fr.blossom.core.common.dto.AbstractAssociationDTO;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.entity.AbstractAssociationEntity;
import fr.blossom.core.common.entity.AbstractEntity;
import fr.blossom.core.common.event.AfterDissociatedEvent;
import fr.blossom.core.common.event.AssociatedEvent;
import fr.blossom.core.common.event.BeforeDissociatedEvent;
import fr.blossom.core.common.mapper.DTOMapper;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericAssociationServiceImpl<
  A extends AbstractDTO, B extends AbstractDTO,
  DTO extends AbstractAssociationDTO<A, B>,
  A_ENTITY extends AbstractEntity, B_ENTITY extends AbstractEntity,
  ENTITY extends AbstractAssociationEntity<A_ENTITY, B_ENTITY>>
  implements AssociationService<A, B, DTO> {

  private final AssociationDao<A_ENTITY, B_ENTITY, ENTITY> dao;
  private final DTOMapper<ENTITY, DTO> mapper;
  private final DTOMapper<A_ENTITY, A> aMapper;
  private final DTOMapper<B_ENTITY, B> bMapper;
  private final ApplicationEventPublisher eventPublisher;

  protected GenericAssociationServiceImpl(AssociationDao<A_ENTITY, B_ENTITY, ENTITY> dao,
    DTOMapper<ENTITY, DTO> mapper, DTOMapper<A_ENTITY, A> aMapper, DTOMapper<B_ENTITY, B> bMapper,
    ApplicationEventPublisher eventPublisher) {
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
    DTO dto  = this.mapper.mapEntity(this.dao.getOne(aMapper.mapDto(a), bMapper.mapDto(b)));
    eventPublisher.publishEvent(new BeforeDissociatedEvent<DTO>(this,dto));
    this.dao.dissociate(aMapper.mapDto(a), bMapper.mapDto(b));
    eventPublisher.publishEvent(new AfterDissociatedEvent<DTO>(this,dto));
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
