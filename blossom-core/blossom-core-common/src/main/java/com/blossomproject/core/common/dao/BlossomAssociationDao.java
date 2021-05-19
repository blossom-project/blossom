package com.blossomproject.core.common.dao;

import com.blossomproject.core.common.entity.AbstractAssociationEntity;
import com.blossomproject.core.common.entity.AbstractEntity;

import java.util.List;

public interface BlossomAssociationDao<A extends AbstractEntity, B extends AbstractEntity, ASSOCIATION extends AbstractAssociationEntity<A, B>>
        extends AssociationDao<A, B, ASSOCIATION> {

    ASSOCIATION associate(Long aId, B b);

    ASSOCIATION associate(Long aId, Long bId, ASSOCIATION association);

    ASSOCIATION associate(Long aId, Long bId);

    ASSOCIATION associate(A a, Long bId);

    ASSOCIATION associate(A a, B b, ASSOCIATION association);

    ASSOCIATION getOne(Long aId, Long bId);

    List<ASSOCIATION> getAll(List<Long> ids);

    ASSOCIATION update(Long id, ASSOCIATION association);

}
