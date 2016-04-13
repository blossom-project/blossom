package fr.mgargadennec.blossom.core.common.support.event.entity;

import org.springframework.context.ApplicationEvent;

import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;

@SuppressWarnings("serial")
public abstract class BlossomEntityEvent<T extends IBlossomServiceDTO> extends ApplicationEvent {

  protected T entity;

  public BlossomEntityEvent(Object source, T entity) {
    super(source);
    this.entity = entity;
  }

  public T getEntity() {
    return entity;
  }

}