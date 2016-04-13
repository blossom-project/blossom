/**
 *
 */
package fr.mgargadennec.blossom.core.common.support.history.batch;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.common.dao.history.IBlossomRevisionDAO;

/**
 * @author pporcher
 *
 */
public class BlossomBatchHistoryPurgeCron {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlossomBatchHistoryPurgeCron.class);

  private int retentionDays;
  private IBlossomRevisionDAO blossomRevisionDao;

  protected BlossomBatchHistoryPurgeCron() {
  }

  public BlossomBatchHistoryPurgeCron(int retentionDays, IBlossomRevisionDAO blossomRevisionDao) {
    this.retentionDays = retentionDays;
    this.blossomRevisionDao = blossomRevisionDao;
  }

  // Tous les jours � 4h15
  @Scheduled(cron = "15 4 * * * ?")
  @Transactional
  public void purge() {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, -retentionDays);

    long timestamp = calendar.getTime().getTime();

    List<Integer> deletedRevList = blossomRevisionDao.deleteByTimestampLessThanEqual(timestamp);

    LOGGER.info("Purge des revisions de l'historique de plus de " + retentionDays + " jours --> "
        + deletedRevList.size() + " suppression(s)");
  }
}
