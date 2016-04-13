package fr.mgargadennec.blossom.core.common.service.generic.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericReadProcess;
import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;
import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericReadService;

public abstract class BlossomGenericReadServiceImpl<I extends BlossomAbstractProcessDTO, O extends IBlossomServiceDTO> implements
    IBlossomGenericReadService<I, O> {

  protected IBlossomGenericReadProcess<?, I> blossomGenericReadProcess;

  public BlossomGenericReadServiceImpl(IBlossomGenericReadProcess<?, I> BlossomGenericProcess) {
    this.blossomGenericReadProcess = BlossomGenericProcess;
  }

  public Page<O> pageServiceDTOFromPageProcessDTO(Page<I> boProcessDTOPage, Pageable pageable) {
    List<O> serviceDTOList = 
    		FluentIterable.from(boProcessDTOPage).transform(new Function<I, O>() {
      public O apply(I processDTO) {
        return createServiceDTOfromProcessDTO(processDTO);
      }
    }).toList();

    return new PageImpl<O>(serviceDTOList, pageable, boProcessDTOPage.getTotalElements());
  }

  public Page<O> getAll(Pageable pageable) {
    Page<I> boProcessDTOPage = blossomGenericReadProcess.getAll(pageable);
    return pageServiceDTOFromPageProcessDTO(boProcessDTOPage, pageable);
  }

  public O get(Long id) {
    I processDTO = blossomGenericReadProcess.get(id);
    return createServiceDTOfromProcessDTO(processDTO);
  }

}