package fr.mgargadennec.blossom.core.common.process.generic;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;

public interface IBlossomGenericProcess<I, O extends BlossomAbstractProcessDTO> extends IBlossomGenericReadProcess<I, O> {

  O create(O processDTO);

  O update(O processDTO);

  void delete(Long id);
}
