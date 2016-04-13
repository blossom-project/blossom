package fr.mgargadennec.blossom.core.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;

public interface IBlossomService<I extends BlossomAbstractProcessDTO, O extends IBlossomServiceDTO> extends IBlossomServicePlugin {

  O createServiceDTOfromProcessDTO(I i);

  I createProcessDTOfromServiceDTO(O o);
  
  Page<O> getAll(Pageable pageable);

  O get(Long id);

}
