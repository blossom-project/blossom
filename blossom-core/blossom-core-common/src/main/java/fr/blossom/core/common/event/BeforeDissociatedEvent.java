package fr.blossom.core.common.event;


import fr.blossom.core.common.dto.AbstractAssociationDTO;

/**
 * Application Event which notify the application when an association is soon going to be deleted.
 *
 * @param <DTO> the {@code AbstractAssociationDTO} just before it is deleted
 * @author Maël Gargadennnec
 */
public class BeforeDissociatedEvent<DTO extends AbstractAssociationDTO> extends Event<DTO> {

  public BeforeDissociatedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
