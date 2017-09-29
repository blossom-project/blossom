package fr.mgargadennec.blossom.core.common.event;


import fr.mgargadennec.blossom.core.common.dto.AbstractAssociationDTO;

public class AssociatedEvent<DTO extends AbstractAssociationDTO> extends Event<DTO> {

  public AssociatedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
