package fr.blossom.core.common.event;


import fr.blossom.core.common.dto.AbstractDTO;

public class UpdatedEvent<DTO extends AbstractDTO> extends Event<DTO> {

  public UpdatedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
