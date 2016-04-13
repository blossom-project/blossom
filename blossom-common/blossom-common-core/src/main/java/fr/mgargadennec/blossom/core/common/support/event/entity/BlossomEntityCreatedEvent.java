package fr.mgargadennec.blossom.core.common.support.event.entity;

import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;

@SuppressWarnings("serial")
public class BlossomEntityCreatedEvent<T extends IBlossomServiceDTO> extends BlossomEntityEvent<T> {

  public BlossomEntityCreatedEvent(Object source, T entity) {
    super(source, entity);
  }
}
