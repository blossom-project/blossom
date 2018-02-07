package com.blossom_project.core.common.search;

import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.event.BeforeDeletedEvent;
import com.blossom_project.core.common.event.CreatedEvent;
import com.blossom_project.core.common.event.UpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listeners listening to various {@link com.blossom_project.core.common.event.Event} and calling IndexationEngine to update the indices.
 *
 *  @author Maël Gargadennnec
 */
public class IndexationEventListeners {
  private final static Logger logger = LoggerFactory.getLogger(IndexationEventListeners.class);
  private final PluginRegistry<IndexationEngine, Class<? extends AbstractDTO>> indexationEngines;

  public IndexationEventListeners(PluginRegistry<IndexationEngine, Class<? extends AbstractDTO>> indexationEngines) {
    this.indexationEngines = indexationEngines;
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleEntityCreation(CreatedEvent createdEvent) {
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

  @EventListener
  public void handleEntityDeletion(BeforeDeletedEvent deletedEvent) {
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

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleEntityUpdate(UpdatedEvent updatedEvent) {
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
