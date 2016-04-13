package fr.mgargadennec.blossom.core.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;

@Repository
public interface BlossomRevisionRepository extends JpaRepository<BlossomRevisionEntity, Integer> {

  List<Integer> deleteByTimestampLessThanEqual(Long timestamp);
}
