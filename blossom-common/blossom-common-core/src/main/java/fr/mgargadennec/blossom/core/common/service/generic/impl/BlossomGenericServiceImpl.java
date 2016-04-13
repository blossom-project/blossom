package fr.mgargadennec.blossom.core.common.service.generic.impl;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericProcess;
import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;
import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericService;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityAfterDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityBeforeDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityCreatedEvent;

public abstract class BlossomGenericServiceImpl<I extends BlossomAbstractProcessDTO, O extends BlossomAbstractServiceDTO>
		extends BlossomGenericReadServiceImpl<I, O> implements IBlossomGenericService<I, O> {

	protected ApplicationEventPublisher eventPublisher;
	protected IBlossomGenericProcess<?, I> blossomGenericProcess;

	public BlossomGenericServiceImpl(IBlossomGenericProcess<?, I> blossomGenericProcess,
			ApplicationEventPublisher eventPublisher) {
		super(blossomGenericProcess);
		this.blossomGenericProcess = blossomGenericProcess;
		this.eventPublisher=eventPublisher;
	}

	@Transactional
	public O create(O boServiceDTO) {
		I processDTOToCreate = createProcessDTOfromServiceDTO(boServiceDTO);
		I createdProcessDTO = blossomGenericProcess.create(processDTOToCreate);

		O result = createServiceDTOfromProcessDTO(createdProcessDTO);

		doPublishEvent(new BlossomEntityCreatedEvent<O>(this, result));

		return result;
	}

	@Transactional
	public void delete(Long id) {
		O result = createServiceDTOfromProcessDTO(blossomGenericReadProcess.get(id));
		doPublishEvent(new BlossomEntityBeforeDeleteEvent<O>(this, result));
		blossomGenericProcess.delete(id);
		doPublishEvent(new BlossomEntityAfterDeleteEvent<O>(this, result));
	}

	protected void doPublishEvent(ApplicationEvent event) {
		eventPublisher.publishEvent(event);
	}

	protected void fillServiceDTOFromProcessDTO(O serviceDTO, I processDTO) {
		serviceDTO.setDateCreation(processDTO.getDateCreation());
		serviceDTO.setDateModification(processDTO.getDateModification());
		serviceDTO.setId(processDTO.getId());
		serviceDTO.setUserCreation(processDTO.getUserCreation());
		serviceDTO.setUserModification(processDTO.getUserModification());
	}

	protected void fillProcessDTOFromServiceDTO(I processDTO, O serviceDTO) {
		processDTO.setDateCreation(serviceDTO.getDateCreation());
		processDTO.setDateModification(serviceDTO.getDateModification());
		processDTO.setId(serviceDTO.getId());
		processDTO.setUserCreation(serviceDTO.getUserCreation());
		processDTO.setUserModification(serviceDTO.getUserModification());
	}

}
