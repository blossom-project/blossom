package fr.blossom.core.common.event;


import fr.blossom.core.common.dto.AbstractDTO;

/**
 * Application Event which notify the application when an entity is going to be deleted.
 *
 * @param <DTO> the {@code AbstractDTO} just before it is deleted
 * @author Maël Gargadennnec
 */
public class BeforeDeletedEvent<DTO extends AbstractDTO> extends Event<DTO> {

  public BeforeDeletedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
