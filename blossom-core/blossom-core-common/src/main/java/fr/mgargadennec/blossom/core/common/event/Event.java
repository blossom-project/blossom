package fr.mgargadennec.blossom.core.common.event;


import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import org.springframework.context.ApplicationEvent;

public abstract class Event<DTO extends AbstractDTO> extends ApplicationEvent {

  protected DTO dto;

  public Event(Object source, DTO dto) {
    super(source);
    this.dto = dto;
  }

  public DTO getDTO() {
    return dto;
  }

}
