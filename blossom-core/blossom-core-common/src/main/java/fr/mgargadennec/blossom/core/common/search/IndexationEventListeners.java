package fr.mgargadennec.blossom.core.common.search;

import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import fr.mgargadennec.blossom.core.common.event.CreatedEvent;
import fr.mgargadennec.blossom.core.common.event.DeletedEvent;
import fr.mgargadennec.blossom.core.common.event.UpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created by Maël Gargadennnec on 28/06/2017.
 */
public class IndexationEventListeners {
  private final static Logger logger = LoggerFactory.getLogger(IndexationEventListeners.class);
  private final PluginRegistry<IndexationEngine, Class<? extends AbstractDTO>> indexationEngines;

  public IndexationEventListeners(PluginRegistry<IndexationEngine, Class<? extends AbstractDTO>> indexationEngines) {
    this.indexationEngines = indexationEngines;
  }

  @TransactionalEventListener
  public void handleEntityCreation(CreatedEvent<? extends AbstractDTO> createdEvent) {
    Class<? extends AbstractDTO> clazz = createdEvent.getDTO() == null ? null : createdEvent.getDTO().getClass();
    if (clazz != null && indexationEngines.hasPluginFor(clazz)) {
      indexationEngines.getPluginFor(clazz).indexOne(createdEvent.getDTO().getId());
      if(logger.isDebugEnabled()) {
        logger.debug("Created event for object {} with id {} received and processed", clazz, createdEvent.getDTO().getId());
      }
    }else{
      if(logger.isDebugEnabled()) {
        logger.debug("Created event for object {} with id {} received but no indexation engine capable to process it", clazz, createdEvent.getDTO().getId());
      }
    }
  }

  @TransactionalEventListener
  public void handleEntityDeletion(DeletedEvent<? extends AbstractDTO> deletedEvent) {
    Class<? extends AbstractDTO> clazz = deletedEvent.getDTO() == null ? null : deletedEvent.getDTO().getClass();
    if (clazz != null && indexationEngines.hasPluginFor(clazz)) {
      indexationEngines.getPluginFor(clazz).deleteOne(deletedEvent.getDTO().getId());
      if(logger.isDebugEnabled()) {
        logger.debug("Deleted event for object {} with id {} received and processed", clazz, deletedEvent.getDTO().getId());
      }
    }else{
      if(logger.isDebugEnabled()) {
        logger.debug("Deleted event for object {} with id {} received but no indexation engine capable to process it", clazz, deletedEvent.getDTO().getId());
      }
    }
  }

  @TransactionalEventListener
  public void handleEntityUpdate(UpdatedEvent<? extends AbstractDTO> updatedEvent) {
    Class<? extends AbstractDTO> clazz = updatedEvent.getDTO() == null ? null : updatedEvent.getDTO().getClass();
    if (clazz != null && indexationEngines.hasPluginFor(clazz)) {
      indexationEngines.getPluginFor(clazz).indexOne(updatedEvent.getDTO().getId());
      if(logger.isDebugEnabled()) {
        logger.debug("Updated event for object {} with id {} received and processed", clazz, updatedEvent.getDTO().getId());
      }
    }else{
      if(logger.isDebugEnabled()) {
        logger.debug("Updated event for object {} with id {} received but no indexation engine capable to process it", clazz, updatedEvent.getDTO().getId());
      }
    }
  }
}
