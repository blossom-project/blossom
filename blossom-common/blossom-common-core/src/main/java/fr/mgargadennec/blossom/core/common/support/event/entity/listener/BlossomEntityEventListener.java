package fr.mgargadennec.blossom.core.common.support.event.entity.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityAfterDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityBeforeDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityCreatedEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityUpdatedEvent;

public interface BlossomEntityEventListener<T extends BlossomAbstractServiceDTO> {
	
	@EventListener
	default void afterDeleteEventListener(BlossomEntityAfterDeleteEvent<T> event) {
		LoggerHolder.logger.debug("After entity deleted : {} with id {}. Executed from class {}.", event.getEntity().getClass(),
				event.getEntity().getId(), event.getSource().getClass());
	}

	@EventListener
	default void beforeDeleteEventListener(BlossomEntityBeforeDeleteEvent<T> event) {
		LoggerHolder.logger.debug("Before entity deleted : {} with id {}. Executed from class {}.", event.getEntity().getClass(),
				event.getEntity().getId(), event.getSource().getClass());
	}

	@EventListener
	default void createdEventListener(BlossomEntityCreatedEvent<T> event) {
		LoggerHolder.logger.debug("After entity created : {} with id {}. Executed from class {}.", event.getEntity().getClass(),
				event.getEntity().getId(), event.getSource().getClass());
	}

	@EventListener
	default void updatedEventListener(BlossomEntityUpdatedEvent<T> event) {
		LoggerHolder.logger.debug("Updated entity : {} with id {}. Executed from class {}.", event.getEntity().getClass(),
				event.getEntity().getId(), event.getSource().getClass());
	}
	
}

final class LoggerHolder {
    static final Logger logger = LoggerFactory.getLogger(BlossomEntityEventListener.class);
}
