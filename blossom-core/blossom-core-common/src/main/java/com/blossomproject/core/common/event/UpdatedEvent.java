package com.blossomproject.core.common.event;


import com.blossomproject.core.common.dto.AbstractDTO;

/**
 * Application Event which notify the application when an entity has been updated
 *
 * @param <DTO> the updated {@code AbstractDTO}
 * @author Maël Gargadennnec
 */
public class UpdatedEvent<DTO extends AbstractDTO> extends Event<DTO> {

  public UpdatedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
