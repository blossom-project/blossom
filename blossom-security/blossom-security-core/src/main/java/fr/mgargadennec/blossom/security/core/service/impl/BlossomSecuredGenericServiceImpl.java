package fr.mgargadennec.blossom.security.core.service.impl;

import org.springframework.context.ApplicationEventPublisher;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericProcess;
import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;
import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericService;
import fr.mgargadennec.blossom.core.common.service.generic.impl.BlossomGenericServiceImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public abstract class BlossomSecuredGenericServiceImpl<I extends BlossomAbstractProcessDTO, O extends BlossomAbstractServiceDTO> extends
BlossomGenericServiceImpl<I, O> implements IBlossomGenericService<I, O> {

  protected IBlossomAuthenticationUtilService boAuthenticationUtilService;

  public BlossomSecuredGenericServiceImpl(IBlossomGenericProcess<?, I> blossomGenericProcess,
      IBlossomAuthenticationUtilService boAuthenticationUtilService, ApplicationEventPublisher eventPublisher) {
    super(blossomGenericProcess, eventPublisher);
    this.boAuthenticationUtilService = boAuthenticationUtilService;
  }

}
