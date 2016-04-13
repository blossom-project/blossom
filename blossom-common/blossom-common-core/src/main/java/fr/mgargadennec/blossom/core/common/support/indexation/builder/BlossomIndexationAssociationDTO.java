package fr.mgargadennec.blossom.core.common.support.indexation.builder;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.common.service.IBlossomService;
import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;
import fr.mgargadennec.blossom.core.common.support.BlossomAssociationMasterSlaveDTO;

public class BlossomIndexationAssociationDTO<K extends IBlossomServiceDTO> {

	private String associationResourceType;
	private String associationName;
	private IBlossomService<?, K> associationService;
	private Function<K, BlossomAssociationMasterSlaveDTO> converter;

	public BlossomIndexationAssociationDTO(String associationResourceType, String associationName,
			IBlossomService<?, K> associationService, Function<K, BlossomAssociationMasterSlaveDTO> converter) {
		this.associationResourceType = associationResourceType;
		this.associationName = associationName;
		this.associationService = associationService;
		this.converter = converter;
	}

	public List<BlossomAssociationMasterSlaveDTO> getAllAssociations() {
		List<K> allAsso = associationService.getAll(null).getContent();
		return Lists.transform(allAsso, converter);
	}

	public String getAssociationResourceType() {
		return associationResourceType;
	}

	public String getAssociationName() {
		return associationName;
	}

	public boolean supports(Class<? extends IBlossomESResourceIndexBuilder> delimiter) {
		return false;
	}

}