package com.blossomproject.core.common.event;


import com.blossomproject.core.common.dto.AbstractAssociationDTO;

/**
 * Application Event which notify the application when an association has been removed.
 *
 * @param <DTO> the {@code AbstractAssociationDTO} which has been dissociated
 * @author Maël Gargadennnec
 */
public class AfterDissociatedEvent<DTO extends AbstractAssociationDTO> extends Event<DTO> {

  public AfterDissociatedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
