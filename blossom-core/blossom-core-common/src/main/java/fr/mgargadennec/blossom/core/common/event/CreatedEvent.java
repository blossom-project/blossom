package fr.mgargadennec.blossom.core.common.event;


import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;

public class CreatedEvent<DTO extends AbstractDTO> extends Event<DTO> {

  public CreatedEvent(Object source, DTO dto) {
    super(source, dto);
  }
}
