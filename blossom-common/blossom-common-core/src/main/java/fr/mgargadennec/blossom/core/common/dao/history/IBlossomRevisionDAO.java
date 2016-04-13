/**
 *
 */
package fr.mgargadennec.blossom.core.common.dao.history;

import java.util.List;

import org.springframework.data.domain.Sort;

import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;

/**
 * @author pporcher
 *
 */
public interface IBlossomRevisionDAO {

  List<BlossomRevisionEntity> getAll(Sort sort);

  List<Integer> deleteByTimestampLessThanEqual(Long timestamp);
}
