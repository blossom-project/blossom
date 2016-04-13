package fr.mgargadennec.blossom.core.common.process.generic.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import fr.mgargadennec.blossom.core.common.dao.generic.IBlossomGenericReadDao;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericReadProcess;

public abstract class BlossomGenericReadProcessImpl<I extends BlossomAbstractEntity, O extends BlossomAbstractProcessDTO>
    implements IBlossomGenericReadProcess<I, O> {

  private IBlossomGenericReadDao<I> genericDAO;

  public BlossomGenericReadProcessImpl(IBlossomGenericReadDao<I> genericDAO) {
    this.genericDAO = genericDAO;
  }

  public Page<O> pageProcessDTOFromPagePO(Page<I> pagePO, Pageable pageable) {
    List<O> processDTOList = FluentIterable.from(pagePO).transform(new Function<I, O>() {
      public O apply(I po) {
        return createProcessDTOfromPO(po);
      }
    }).toList();

    return new PageImpl<O>(processDTOList, pageable, pagePO.getTotalElements());
  }

  public Page<O> getAll(Pageable pageable) {
    Page<I> boPOPage = genericDAO.getAll(pageable);

    return pageProcessDTOFromPagePO(boPOPage, pageable);
  }

  public O get(Long id) {
    return createProcessDTOfromPO(genericDAO.get(id));
  }

}