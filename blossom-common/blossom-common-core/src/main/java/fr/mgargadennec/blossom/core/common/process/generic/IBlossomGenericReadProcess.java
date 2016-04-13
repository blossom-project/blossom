package fr.mgargadennec.blossom.core.common.process.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;
import fr.mgargadennec.blossom.core.common.process.IBlossomProcess;

public interface IBlossomGenericReadProcess<I, O extends BlossomAbstractProcessDTO> extends IBlossomProcess<I, O> {

  Page<O> pageProcessDTOFromPagePO(Page<I> pagePO, Pageable pageable);

  Page<O> getAll(Pageable pageable);

  O get(Long id);

}