/**
 *
 */
package fr.mgargadennec.blossom.core.common.support.history.reader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryDTO;

/**
 * @author pporcher
 *
 */
public interface IBlossomHistoryReader {

  Page<BlossomHistoryDTO<? extends BlossomAbstractEntity>> search(String query, BlossomHistorySearchFilter filters, Pageable pageable);

  BlossomHistoryDTO<? extends BlossomAbstractEntity> getOne(String historyIdAsString);

}
