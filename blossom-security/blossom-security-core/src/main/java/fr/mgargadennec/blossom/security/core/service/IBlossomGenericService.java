package fr.mgargadennec.blossom.security.core.service;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;
import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;
import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericReadService;

public interface IBlossomGenericService<I extends BlossomAbstractProcessDTO, O extends IBlossomServiceDTO> extends
    IBlossomGenericReadService<I, O> {

  O create(O boServiceDTO);

  O update(Long id, O boServiceDTO);

  void delete(Long id);

}
