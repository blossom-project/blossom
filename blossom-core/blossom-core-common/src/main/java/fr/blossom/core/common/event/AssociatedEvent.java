package fr.blossom.core.common.event;


import fr.blossom.core.common.dto.AbstractAssociationDTO;

/**
 * Application Event which notify the application when an association has been created.
 *
 * @param <DTO> the {@code AbstractAssociationDTO} which has been creaated
 * @author Maël Gargadennnec
 */
public class AssociatedEvent<DTO extends AbstractAssociationDTO> extends Event<DTO> {

  public AssociatedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
