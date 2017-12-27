package fr.blossom.core.common.event;


import fr.blossom.core.common.dto.AbstractDTO;
import org.springframework.context.ApplicationEvent;

/**
 * Base class for all Blossom events which are related to a {@link AbstractDTO}
 *
 * @param <DTO> the deleted {@code AbstractDTO}
 * @author Maël Gargadennnec
 */
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
