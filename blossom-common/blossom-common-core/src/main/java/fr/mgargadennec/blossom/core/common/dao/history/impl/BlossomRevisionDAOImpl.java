/**
 *
 */
package fr.mgargadennec.blossom.core.common.dao.history.impl;

import java.util.List;

import org.springframework.data.domain.Sort;

import fr.mgargadennec.blossom.core.common.dao.history.IBlossomRevisionDAO;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.repository.BlossomRevisionRepository;

/**
 * @author pporcher
 *
 */
public class BlossomRevisionDAOImpl implements IBlossomRevisionDAO {

  BlossomRevisionRepository blossomRevisionRepository;

  public BlossomRevisionDAOImpl(BlossomRevisionRepository blossomRevisionRepository) {
    this.blossomRevisionRepository = blossomRevisionRepository;
  }

  public List<BlossomRevisionEntity> getAll(Sort sort) {
    return blossomRevisionRepository.findAll(sort);
  }

  public List<Integer> deleteByTimestampLessThanEqual(Long timestamp) {
    return blossomRevisionRepository.deleteByTimestampLessThanEqual(timestamp);
  }

}
