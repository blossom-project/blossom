package fr.blossom.core.common.dao;

import fr.blossom.core.common.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReadOnlyDao<ENTITY extends AbstractEntity> {

    List<ENTITY> getAll();

    Page<ENTITY> getAll(Pageable pageable);

    List<ENTITY> getAll(List<Long> ids);

    ENTITY getOne(long id);

}
