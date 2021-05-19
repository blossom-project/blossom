package com.blossomproject.core.common.service;

import com.blossomproject.core.common.dto.AbstractAssociationDTO;
import com.blossomproject.core.common.dto.AbstractDTO;

import java.util.List;

public interface BlossomAssociationService<A extends AbstractDTO, B extends AbstractDTO, ASSOCIATION extends AbstractAssociationDTO<A, B>>
        extends AssociationService<A, B, ASSOCIATION> {

    ASSOCIATION associate(Long aId, B b);

    ASSOCIATION associate(Long aId, Long bId);

    ASSOCIATION associate(A a, Long bId);

    ASSOCIATION associate(Long aId, Long bId, ASSOCIATION asso);

    ASSOCIATION associate(A a, B b, ASSOCIATION asso);

    void dissociate(List<ASSOCIATION> associations);

    ASSOCIATION dissociate(Long aId, Long bId);

    ASSOCIATION getOne(A a, B b);

    List<ASSOCIATION> getAll(List<Long> ids);

    ASSOCIATION update(Long id, ASSOCIATION association);

}
