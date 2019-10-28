package com.blossomproject.core.common.service;

import com.blossomproject.core.common.dao.BlossomAssociationDao;
import com.blossomproject.core.common.dto.AbstractAssociationDTO;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.entity.AbstractAssociationEntity;
import com.blossomproject.core.common.entity.AbstractEntity;
import com.blossomproject.core.common.event.AfterDissociatedEvent;
import com.blossomproject.core.common.event.AssociatedEvent;
import com.blossomproject.core.common.event.BeforeDissociatedEvent;
import com.blossomproject.core.common.mapper.DTOMapper;
import org.springframework.context.ApplicationEventPublisher;

import javax.transaction.Transactional;
import java.util.List;

public abstract class BlossomGenericAssociationServiceImpl<
        A extends AbstractDTO, B extends AbstractDTO,
        DTO extends AbstractAssociationDTO<A, B>,
        A_ENTITY extends AbstractEntity, B_ENTITY extends AbstractEntity,
        ENTITY extends AbstractAssociationEntity<A_ENTITY, B_ENTITY>
        >
        extends GenericAssociationServiceImpl<A, B, DTO, A_ENTITY, B_ENTITY, ENTITY>
        implements BlossomAssociationService<A, B, DTO> {

    private final BlossomAssociationDao<A_ENTITY, B_ENTITY, ENTITY> dao;
    private final DTOMapper<ENTITY, DTO> mapper;
    private final DTOMapper<A_ENTITY, A> aMapper;
    private final DTOMapper<B_ENTITY, B> bMapper;
    private final ApplicationEventPublisher eventPublisher;

    protected BlossomGenericAssociationServiceImpl(BlossomAssociationDao<A_ENTITY, B_ENTITY, ENTITY> dao,
            DTOMapper<ENTITY, DTO> mapper,
            DTOMapper<A_ENTITY, A> aMapper,
            DTOMapper<B_ENTITY, B> bMapper,
            ApplicationEventPublisher eventPublisher) {
        super(dao, mapper, aMapper, bMapper, eventPublisher);
        this.dao = dao;
        this.mapper = mapper;
        this.aMapper = aMapper;
        this.bMapper = bMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public DTO associate(Long aId, B b) {
        DTO dto = mapper.mapEntity(dao.associate(aId, bMapper.mapDto(b)));
        eventPublisher.publishEvent(new AssociatedEvent<>(this, dto));
        return dto;
    }

    @Override
    @Transactional
    public DTO associate(Long aId, Long bId) {
        DTO dto = mapper.mapEntity(dao.associate(aId, bId));
        eventPublisher.publishEvent(new AssociatedEvent<>(this, dto));
        return dto;
    }

    @Override
    @Transactional
    public DTO associate(A a, Long bId) {
        DTO dto = mapper.mapEntity(dao.associate(aMapper.mapDto(a), bId));
        eventPublisher.publishEvent(new AssociatedEvent<>(this, dto));
        return dto;
    }

    @Override
    @Transactional
    public DTO associate(Long aId, Long bId, DTO asso) {
        DTO dto = mapper.mapEntity(dao.associate(aId, bId, mapper.mapDto(asso)));
        eventPublisher.publishEvent(new AssociatedEvent<>(this, dto));
        return dto;
    }

    @Override
    @Transactional
    public DTO associate(A a, B b, DTO asso) {
        DTO dto = mapper.mapEntity(dao.associate(aMapper.mapDto(a), bMapper.mapDto(b), mapper.mapDto(asso)));
        eventPublisher.publishEvent(new AssociatedEvent<>(this, dto));
        return dto;
    }

    @Override
    @Transactional
    public void dissociate(List<DTO> associations) {
        associations.forEach(association -> this.dissociate(association.getA(), association.getB()));
    }

    @Override
    @Transactional
    public DTO dissociate(Long aId, Long bId) {
        DTO dto = this.mapper.mapEntity(this.dao.getOne(aId, bId));
        if (dto != null) {
            eventPublisher.publishEvent(new BeforeDissociatedEvent<>(this, dto));
            this.dao.dissociate(aMapper.mapDto(dto.getA()), bMapper.mapDto(dto.getB()));
            eventPublisher.publishEvent(new AfterDissociatedEvent<>(this, dto));
        }
        return dto;
    }

    @Override
    @Transactional
    public DTO getOne(A a, B b) {
        return mapper.mapEntity(dao.getOne(aMapper.mapDto(a), bMapper.mapDto(b)));
    }

    @Override
    public List<DTO> getAll(List<Long> ids) {
        return mapper.mapEntities(dao.getAll(ids));
    }

    @Override
    public DTO update(Long id, DTO dto) {
        return mapper.mapEntity(dao.update(id, mapper.mapDto(dto)));
    }
}
