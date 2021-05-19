package com.blossomproject.core.common.dao;

import com.blossomproject.core.common.entity.AbstractAssociationEntity;
import com.blossomproject.core.common.entity.AbstractEntity;
import com.blossomproject.core.common.mapper.BlossomEntityMapper;
import com.blossomproject.core.common.repository.AssociationRepository;
import com.google.common.base.Preconditions;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

public abstract class BlossomGenericAssociationDaoImpl<
        A extends AbstractEntity,
        B extends AbstractEntity,
        ASSOCIATION extends AbstractAssociationEntity<A, B>
        >
        extends GenericAssociationDaoImpl<A, B, ASSOCIATION>
        implements BlossomAssociationDao<A, B, ASSOCIATION> {

    private final JpaRepository<A, Long> aRepository;
    private final JpaRepository<B, Long> bRepository;
    private final AssociationRepository<A, B, ASSOCIATION> repository;
    private final BlossomEntityMapper<ASSOCIATION> mergeMapper;

    protected BlossomGenericAssociationDaoImpl(JpaRepository<A, Long> aRepository,
            JpaRepository<B, Long> bRepository,
            AssociationRepository<A, B, ASSOCIATION> repository,
            BlossomEntityMapper<ASSOCIATION> mergeMapper) {
        super(repository);
        this.aRepository = aRepository;
        this.bRepository = bRepository;
        this.repository = repository;
        this.mergeMapper = mergeMapper;
    }

    @Override
    @Transactional
    public ASSOCIATION associate(Long aId, B b) {
        return associate(aRepository.getOne(aId), b);
    }

    @Override
    @Transactional
    public ASSOCIATION associate(Long aId, Long bId, ASSOCIATION association) {
        return associate(aRepository.getOne(aId), bRepository.getOne(bId), association);
    }

    @Override
    @Transactional
    public ASSOCIATION associate(Long aId, Long bId) {
        return associate(aRepository.getOne(aId), bRepository.getOne(bId));
    }

    @Override
    @Transactional
    public ASSOCIATION associate(A a, Long bId) {
        return associate(a, bRepository.getOne(bId));
    }

    @Override
    @Transactional
    public ASSOCIATION associate(A a, B b) {
        return associate(a, b, create());
    }

    @Override
    @Transactional
    public ASSOCIATION associate(A a, B b, ASSOCIATION association) {
        Preconditions.checkArgument(a != null);
        Preconditions.checkArgument(b != null);

        if (a.getId() != null) {
            try {
                a = aRepository.getOne(a.getId());
            } catch (EntityNotFoundException e) {
                // Ignore and save new entity
            }
        }
        if (b.getId() != null) {
            try {
                b = bRepository.getOne(b.getId());
            } catch (EntityNotFoundException e) {
                // Ignore and save new entity
            }
        }

        ASSOCIATION existing = repository.findOneByAAndB(a, b);
        if (existing != null) {
            return existing;
        }
        association.setA(a);
        association.setB(b);

        return repository.save(association);
    }

    @Override
    @Transactional
    public List<ASSOCIATION> getAllA(B b) {
        return super.getAllA(b);
    }

    @Override
    @Transactional
    public List<ASSOCIATION> getAllB(A a) {
        return super.getAllB(a);
    }

    @Override
    @Transactional
    public ASSOCIATION getOne(long id) {
        return super.getOne(id);
    }

    @Override
    @Transactional
    public ASSOCIATION getOne(A a, B b) {
        return super.getOne(a, b);
    }

    @Override
    @Transactional
    public ASSOCIATION getOne(Long aId, Long bId) {
        return repository.findOneByAAndB(aRepository.getOne(aId), bRepository.getOne(bId));
    }

    @Override
    @Transactional
    public List<ASSOCIATION> getAll(List<Long> ids) {
        return repository.findAllById(ids);
    }

    @Override
    @Transactional
    public ASSOCIATION update(Long id, ASSOCIATION association) {
        return repository.save(merge(repository.getOne(id), association));
    }

    protected ASSOCIATION merge(ASSOCIATION originalEntity, ASSOCIATION modifiedEntity) {
        return mergeMapper.merge(originalEntity, modifiedEntity);
    }
}
