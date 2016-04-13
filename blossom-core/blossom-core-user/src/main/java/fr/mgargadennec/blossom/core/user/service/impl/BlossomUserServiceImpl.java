package fr.mgargadennec.blossom.core.user.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityCreatedEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityUpdatedEvent;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.IBlossomElasticsearchQueryService;
import fr.mgargadennec.blossom.core.user.constants.BlossomUserConst;
import fr.mgargadennec.blossom.core.user.model.BlossomUserStateEnum;
import fr.mgargadennec.blossom.core.user.process.IBlossomUserProcess;
import fr.mgargadennec.blossom.core.user.process.dto.BlossomUserProcessDTO;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.core.user.service.dto.BlossomUserServiceDTO;
import fr.mgargadennec.blossom.security.core.service.impl.BlossomSecuredGenericServiceImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public class BlossomUserServiceImpl extends
		BlossomSecuredGenericServiceImpl<BlossomUserProcessDTO, BlossomUserServiceDTO> implements IBlossomUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BlossomUserServiceImpl.class);

	private IBlossomUserProcess blossomUserProcess;
	private IBlossomElasticsearchQueryService esService;
	private IBlossomESResourceIndexBuilder boUserESResourceIndexBuilder;

	public BlossomUserServiceImpl(IBlossomUserProcess userProcess,
			IBlossomElasticsearchQueryService esService,
			IBlossomAuthenticationUtilService boAuthenticationUtilService, 
			ApplicationEventPublisher eventPublisher,
			IBlossomESResourceIndexBuilder boUserESResourceIndexBuilder) {
		super(userProcess, boAuthenticationUtilService, eventPublisher);
		this.esService = esService;
		this.boUserESResourceIndexBuilder = boUserESResourceIndexBuilder;
	}

	public Page<BlossomUserServiceDTO> search(String q, Pageable pageable) {

		QueryBuilder query = QueryBuilders.multiMatchQuery(q, "id", "firstname", "lastname", "login", "email")
				.type(Type.CROSS_FIELDS).lenient(true);

		SearchRequestBuilder searchRequest = null;

		searchRequest = esService.prepareSearchRequest(boUserESResourceIndexBuilder.getIndexInfos(), query,
				pageable.getPageSize(), pageable.getOffset());

		Sort sort = pageable.getSort();
		if (sort != null) {
			for (Order order : pageable.getSort()) {
				searchRequest.addSort(SortBuilders.fieldSort(order.getProperty())
						.order(SortOrder.valueOf(order.getDirection().name())));
			}
			searchRequest.addSort(SortBuilders.scoreSort());
		}

		LOGGER.debug(searchRequest.toString());

		SearchResponse searchResponse = searchRequest.get();

		List<BlossomUserServiceDTO> users = Lists.newArrayList();
		for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
			try {
				users.add(new ObjectMapper().readValue(searchResponse.getHits().getHits()[i].getSourceAsString(),
						BlossomUserServiceDTO.class));
			} catch (IOException e) {
				throw new RuntimeException("Can't parse user hit content", e);
			}
		}

		return new PageImpl<BlossomUserServiceDTO>(users, pageable, searchResponse.getHits().getTotalHits());
	}

	public Page<BlossomUserServiceDTO> search(String q, String associationName, String associationId,
			Pageable pageable) {

		QueryBuilder query = null;
		if (Strings.isNullOrEmpty(q)) {
			query = QueryBuilders.matchAllQuery();
		} else {
			query = QueryBuilders.multiMatchQuery(q, "id", "firstname", "lastname", "login", "email")
					.type(Type.CROSS_FIELDS).lenient(true);
		}
		if (Strings.isNullOrEmpty(associationName) || Strings.isNullOrEmpty(associationId)) {
			return search(q, pageable);
		}

		FilteredQueryBuilder queryFiltered = QueryBuilders.filteredQuery(query,
				FilterBuilders.termsFilter("associations." + associationName, associationId));

		SearchRequestBuilder searchRequest = esService.prepareSearchRequest(
				boUserESResourceIndexBuilder.getIndexInfos(), queryFiltered, pageable.getPageSize(),
				pageable.getOffset());

		Sort sort = pageable.getSort();
		if (sort != null) {
			for (Order order : pageable.getSort()) {
				searchRequest.addSort(SortBuilders.fieldSort(order.getProperty())
						.order(SortOrder.valueOf(order.getDirection().name())));
			}
			searchRequest.addSort(SortBuilders.scoreSort());
		}

		LOGGER.debug(searchRequest.toString());

		SearchResponse searchResponse = searchRequest.get();

		List<BlossomUserServiceDTO> users = Lists.newArrayList();
		for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
			try {
				users.add(new ObjectMapper().readValue(searchResponse.getHits().getHits()[i].getSourceAsString(),
						BlossomUserServiceDTO.class));
			} catch (IOException e) {
				throw new RuntimeException("Can't parse user hit content", e);
			}
		}

		return new PageImpl<BlossomUserServiceDTO>(users, pageable, searchResponse.getHits().getTotalHits());
	}

	@Transactional
	public BlossomUserServiceDTO create(BlossomUserServiceDTO boUserServiceDTO) {

		boUserServiceDTO.setPassword(null);
		boUserServiceDTO.setState(BlossomUserStateEnum.INITIALIZED);
		BlossomUserProcessDTO processDTOToCreate = createProcessDTOfromServiceDTO(boUserServiceDTO);
		BlossomUserProcessDTO createdProcessDTO = blossomUserProcess.create(processDTOToCreate);

		BlossomUserServiceDTO createdServiceDTO = createServiceDTOfromProcessDTO(createdProcessDTO);

		doPublishEvent(new BlossomEntityCreatedEvent<BlossomUserServiceDTO>(this, createdServiceDTO));

		return createdServiceDTO;
	}

	@Transactional
	public BlossomUserServiceDTO update(Long userId, BlossomUserServiceDTO boUserUpdateServiceDTO) {
		BlossomUserProcessDTO boUserProcessDTOtoUpdate = blossomUserProcess.get(userId);

		boUserProcessDTOtoUpdate.setFirstname(boUserUpdateServiceDTO.getFirstname());
		boUserProcessDTOtoUpdate.setLastname(boUserUpdateServiceDTO.getLastname());
		boUserProcessDTOtoUpdate.setEmail(boUserUpdateServiceDTO.getEmail());
		boUserProcessDTOtoUpdate.setPhone(boUserUpdateServiceDTO.getPhone());
		boUserProcessDTOtoUpdate.setFunction(boUserUpdateServiceDTO.getFunction());
		boUserProcessDTOtoUpdate.setState(boUserUpdateServiceDTO.getState() != null ? boUserUpdateServiceDTO.getState()
				: boUserProcessDTOtoUpdate.getState());

		BlossomUserProcessDTO updatedUserProcessDTO = blossomUserProcess.update(boUserProcessDTOtoUpdate);

		BlossomUserServiceDTO result = createServiceDTOfromProcessDTO(updatedUserProcessDTO);

		doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, result));

		return result;
	}

	public BlossomUserServiceDTO getByLogin(String login) throws BadCredentialsException {
		return createServiceDTOfromProcessDTO(blossomUserProcess.getByLogin(login));
	}

	public BlossomUserServiceDTO createServiceDTOfromProcessDTO(BlossomUserProcessDTO i) {
		if (i == null) {
			return null;
		}

		BlossomUserServiceDTO result = new BlossomUserServiceDTO();
		result.setId(i.getId());
		result.setFirstname(i.getFirstname());
		result.setLastname(i.getLastname());
		result.setEmail(i.getEmail());
		result.setPhone(i.getPhone());
		result.setLogin(i.getLogin());
		result.setPassword(i.getPassword());
		result.setState(i.getState());
		result.setFunction(i.getFunction());
		result.setRoot(i.getRoot());
		result.setDateCreation(i.getDateCreation());
		result.setDateModification(i.getDateModification());
		result.setUserCreation(i.getUserCreation());
		result.setUserModification(i.getUserModification());

		computePossibleStates(result);

		return result;
	}

	public BlossomUserProcessDTO createProcessDTOfromServiceDTO(BlossomUserServiceDTO o) {
		if (o == null) {
			return null;
		}

		BlossomUserProcessDTO result = new BlossomUserProcessDTO();
		result.setId(o.getId());
		result.setFirstname(o.getFirstname());
		result.setLastname(o.getLastname());
		result.setEmail(o.getEmail());
		result.setPhone(o.getPhone());
		result.setLogin(o.getLogin());
		result.setPassword(o.getPassword());
		result.setState(o.getState());
		result.setFunction(o.getFunction());
		result.setRoot(o.isRoot());
		result.setDateCreation(o.getDateCreation());
		result.setDateModification(o.getDateModification());
		result.setUserCreation(o.getUserCreation());
		result.setUserModification(o.getUserModification());

		return result;
	}


	private void computePossibleStates(BlossomUserServiceDTO boUserServiceDTO) {
		switch (boUserServiceDTO.getState()) {
		case INITIALIZED:
		case LOCKED:
			boUserServiceDTO.setAvailableStates(new ArrayList<BlossomUserStateEnum>());
			break;
		case ENABLED:
			boUserServiceDTO.setAvailableStates(Lists.newArrayList(BlossomUserStateEnum.DISABLED));
			break;
		case DISABLED:
			boUserServiceDTO.setAvailableStates(Lists.newArrayList(BlossomUserStateEnum.ENABLED));
			break;
		default:
			break;
		}
	}

	public boolean supports(String delimiter) {
		return BlossomUserConst.BLOSSOM_USER_ENTITY_NAME.equals(delimiter);
	}
}
