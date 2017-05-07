package fr.mgargadennec.blossom.core.common.service;

import fr.mgargadennec.blossom.core.common.dao.AssociationDao;
import fr.mgargadennec.blossom.core.common.dto.AbstractAssociationDTO;
import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import fr.mgargadennec.blossom.core.common.entity.AbstractAssociationEntity;
import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;
import fr.mgargadennec.blossom.core.common.mapper.DTOMapper;

import java.util.List;

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

    protected GenericAssociationServiceImpl(AssociationDao<A_ENTITY, B_ENTITY, ENTITY> dao, DTOMapper<ENTITY, DTO> mapper, DTOMapper<A_ENTITY, A> aMapper, DTOMapper<B_ENTITY, B> bMapper) {
        this.dao = dao;
        this.mapper = mapper;
        this.aMapper = aMapper;
        this.bMapper = bMapper;
    }

    @Override
    public DTO associate(A a, B b) {
        return this.mapper.mapEntity(this.dao.associate(aMapper.mapDto(a), bMapper.mapDto(b)));
    }

    @Override
    public void dissociate(A a, B b) {
        this.dao.dissociate(aMapper.mapDto(a), bMapper.mapDto(b));
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
        return null;
    }
}
