package fr.blossom.core.common.event;

import fr.blossom.core.common.dto.AbstractDTO;

public class DeletedEvent<DTO extends AbstractDTO> extends Event<DTO> {

  public DeletedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
