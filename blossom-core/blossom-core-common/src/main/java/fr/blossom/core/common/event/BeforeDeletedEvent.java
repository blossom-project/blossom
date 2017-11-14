package fr.blossom.core.common.event;


import fr.blossom.core.common.dto.AbstractDTO;

public class BeforeDeletedEvent<DTO extends AbstractDTO> extends Event<DTO> {

  public BeforeDeletedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
