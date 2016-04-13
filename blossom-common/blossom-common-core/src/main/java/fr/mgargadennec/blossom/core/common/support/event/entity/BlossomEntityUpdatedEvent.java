package fr.mgargadennec.blossom.core.common.support.event.entity;

import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;

@SuppressWarnings("serial")
public class BlossomEntityUpdatedEvent<T extends IBlossomServiceDTO> extends BlossomEntityEvent<T> {

  public BlossomEntityUpdatedEvent(Object source, T entity) {
    super(source, entity);
  }

}
