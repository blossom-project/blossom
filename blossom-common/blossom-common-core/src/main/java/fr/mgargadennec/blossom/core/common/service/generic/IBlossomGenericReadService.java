package fr.mgargadennec.blossom.core.common.service.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;
import fr.mgargadennec.blossom.core.common.service.IBlossomService;
import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;

public interface IBlossomGenericReadService<I extends BlossomAbstractProcessDTO, O extends IBlossomServiceDTO> extends IBlossomService<I, O> {

  Page<O> pageServiceDTOFromPageProcessDTO(Page<I> boProcessDTOPage, Pageable pageable);

  Page<O> getAll(Pageable pageable);

  Page<O> search(String query, Pageable pageable);

  Page<O> search(String query, String associationName, String associationId, Pageable pageable);

}