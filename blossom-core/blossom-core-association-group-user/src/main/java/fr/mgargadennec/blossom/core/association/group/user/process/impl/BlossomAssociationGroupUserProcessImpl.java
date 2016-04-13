package fr.mgargadennec.blossom.core.association.group.user.process.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.group.user.dao.IBlossomAssociationGroupUserDAO;
import fr.mgargadennec.blossom.core.association.group.user.model.BlossomAssociationGroupUserPO;
import fr.mgargadennec.blossom.core.association.group.user.process.IBlossomAssociationGroupUserProcess;
import fr.mgargadennec.blossom.core.association.group.user.process.dto.BlossomAssociationGroupUserProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.impl.BlossomGenericProcessImpl;

/**
 * Couche Process - AssociationGroupUser
 */
public class BlossomAssociationGroupUserProcessImpl
		extends BlossomGenericProcessImpl<BlossomAssociationGroupUserPO, BlossomAssociationGroupUserProcessDTO>
		implements IBlossomAssociationGroupUserProcess {

	IBlossomAssociationGroupUserDAO boAssociationGroupUserDAO;

	public BlossomAssociationGroupUserProcessImpl(IBlossomAssociationGroupUserDAO boAssociationGroupUserDAO) {
		super(boAssociationGroupUserDAO);
		this.boAssociationGroupUserDAO = boAssociationGroupUserDAO;
	}

	public Page<BlossomAssociationGroupUserProcessDTO> getAllByGroupId(Pageable pageable, Long groupId) {
		Page<BlossomAssociationGroupUserPO> boAssociationGroupUserPOPage = boAssociationGroupUserDAO
				.getAllByGroupId(pageable, groupId);
		return pageProcessDTOFromPagePO(boAssociationGroupUserPOPage, pageable);
	}

	public Page<BlossomAssociationGroupUserProcessDTO> getAllByUserId(Pageable pageable, Long userId) {
		Page<BlossomAssociationGroupUserPO> boAssociationGroupUserPOPage = boAssociationGroupUserDAO
				.getAllByUserId(pageable, userId);
		return pageProcessDTOFromPagePO(boAssociationGroupUserPOPage, pageable);
	}

	public Page<BlossomAssociationGroupUserProcessDTO> getAllByGroupIdAndUserId(Pageable pageable, Long groupId,
			Long userId) {
		Page<BlossomAssociationGroupUserPO> boAssociationGroupUserPOPage = boAssociationGroupUserDAO
				.getAllByGroupIdAndUserId(pageable, groupId, userId);
		return pageProcessDTOFromPagePO(boAssociationGroupUserPOPage, pageable);
	}

	public void deleteByGroupId(Long groupId) {
		boAssociationGroupUserDAO.deleteByGroupId(groupId);
	}

	public void deleteByUserId(Long userId) {
		boAssociationGroupUserDAO.deleteByUserId(userId);
	}

	public BlossomAssociationGroupUserProcessDTO createProcessDTOfromPO(BlossomAssociationGroupUserPO po) {
		if (po == null) {
			return null;
		}

		BlossomAssociationGroupUserProcessDTO processDTO = new BlossomAssociationGroupUserProcessDTO();
		fillProcessDTOFromPO(processDTO, po);
		return processDTO;

	}

	protected void fillProcessDTOFromPO(BlossomAssociationGroupUserProcessDTO processDTO,
			BlossomAssociationGroupUserPO po) {
		super.fillProcessDTOFromPO(processDTO, po);
		processDTO.setGroupId(po.getGroupId());
		processDTO.setUserId(po.getUserId());
	}

	public BlossomAssociationGroupUserPO createPOfromProcessDTO(BlossomAssociationGroupUserProcessDTO processDTO) {
		if (processDTO == null) {
			return null;
		}
		BlossomAssociationGroupUserPO po = new BlossomAssociationGroupUserPO();
		fillPOFromProcessDTO(po, processDTO);
		return po;
	}

	protected void fillPOFromProcessDTO(BlossomAssociationGroupUserPO po,
			BlossomAssociationGroupUserProcessDTO processDTO) {
		super.fillPOFromProcessDTO(po, processDTO);
		po.setGroupId(processDTO.getGroupId());
		po.setUserId(processDTO.getUserId());
	}

}
