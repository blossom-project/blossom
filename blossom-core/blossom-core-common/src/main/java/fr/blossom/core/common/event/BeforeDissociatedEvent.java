package fr.blossom.core.common.event;


import fr.blossom.core.common.dto.AbstractAssociationDTO;

public class BeforeDissociatedEvent<DTO extends AbstractAssociationDTO> extends Event<DTO> {

  public BeforeDissociatedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
