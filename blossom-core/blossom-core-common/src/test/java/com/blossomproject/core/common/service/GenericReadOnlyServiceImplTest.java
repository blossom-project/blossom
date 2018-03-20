package com.blossomproject.core.common.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blossomproject.core.common.dao.ReadOnlyDao;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.entity.AbstractEntity;
import com.blossomproject.core.common.mapper.DTOMapper;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class GenericReadOnlyServiceImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  ReadOnlyDao<ENTITY> dao;

  @Mock
  DTOMapper<ENTITY, DTO> mapper;

  @InjectMocks
  TestGenericReadOnlyServiceImpl service;

  @Test
  public void should_get_one_with_null() {
    thrown.expect(NullPointerException.class);
    service.getOne(null);
  }

  @Test
  public void should_get_one_with_id_not_found() {
    when(mapper.mapEntity(eq(null))).thenThrow(new NullPointerException());
    thrown.expect(NullPointerException.class);
    service.getOne(1L);
  }

  @Test
  public void should_get_one_with_success() {
    ENTITY entity = new ENTITY(1L);
    DTO dto = new DTO(1L);

    when(dao.getOne(eq(1L))).thenReturn(entity);
    when(mapper.mapEntity(eq(entity))).thenReturn(dto);

    DTO result = service.getOne(1L);

    assertNotNull(result);
    assertEquals(dto, result);

    verify(dao, times(1)).getOne(anyLong());
  }

  @Test
  public void should_get_all_ids_with_null() {
    when(dao.getAll(nullable(List.class))).thenReturn(Lists.newArrayList());
    when(mapper.mapEntities(any())).thenReturn(Lists.newArrayList());

    List result = service.getAll((List) null);
    assertNotNull(result);
    assertTrue(result.isEmpty());

    verify(dao, times(1)).getAll(nullable(List.class));
  }

  @Test
  public void should_get_all_ids_with_missing_db_entities() {

    List<Long> listIds = Lists.newArrayList(1L, 2L, 3L);
    List<DTO> listDto = Lists.newArrayList(new DTO(1L), new DTO(3L));
    List<ENTITY> listEntity = Lists.newArrayList(new ENTITY(1L), new ENTITY(3L));

    when(dao.getAll(eq(listIds))).thenReturn(listEntity);
    when(mapper.mapEntities(eq(listEntity))).thenReturn(listDto);

    List result = service.getAll(listIds);
    assertNotNull(result);
    assertEquals(listDto, result);

    verify(dao, times(1)).getAll(eq(listIds));
  }

  @Test
  public void should_get_all_ids_with_success() {

    List<Long> listIds = Lists.newArrayList(1L, 3L);
    List<DTO> listDto = Lists.newArrayList(new DTO(1L), new DTO(3L));
    List<ENTITY> listEntity = Lists.newArrayList(new ENTITY(1L), new ENTITY(3L));

    when(dao.getAll(eq(listIds))).thenReturn(listEntity);
    when(mapper.mapEntities(eq(listEntity))).thenReturn(listDto);

    List result = service.getAll(listIds);
    assertNotNull(result);
    assertEquals(listDto, result);

    verify(dao, times(1)).getAll(eq(listIds));
  }

  @Test
  public void should_get_all_pageable_with_null() {
    when(dao.getAll(nullable(Pageable.class))).thenReturn(Page.empty());
    when(mapper.mapEntitiesPage(any())).thenReturn(Page.empty());

    Page result = service.getAll((Pageable) null);
    assertNotNull(result);
    assertFalse(result.hasNext());

    verify(dao, times(1)).getAll(nullable(Pageable.class));
  }

  @Test
  public void should_get_all_pageable_with_success() {
    Pageable pageable = mock(Pageable.class);
    Page<ENTITY> pageEntity = mock(Page.class);
    Page<DTO> pageDto = mock(Page.class);
    when(dao.getAll(any(Pageable.class))).thenReturn(pageEntity);
    when(mapper.mapEntitiesPage(any())).thenReturn(pageDto);

    Page<DTO> result = service.getAll(pageable);
    assertNotNull(result);
    assertEquals(pageDto, result);

    verify(dao, times(1)).getAll(any(Pageable.class));
  }

  @Test
  public void should_get_all_with_no_entities() {
    when(dao.getAll()).thenReturn(Lists.newArrayList());
    when(mapper.mapEntities(any())).thenReturn(Lists.newArrayList());

    List result = service.getAll();
    assertNotNull(result);
    assertTrue(result.isEmpty());

    verify(dao, times(1)).getAll();
  }

  @Test
  public void should_get_all_with_success() {
    List<DTO> listDto = Lists.newArrayList(new DTO(1L), new DTO(2L));
    List<ENTITY> listEntity = Lists.newArrayList(new ENTITY(1L), new ENTITY(2L));
    when(dao.getAll()).thenReturn(listEntity);
    when(mapper.mapEntities(any())).thenReturn(listDto);

    List result = service.getAll();
    assertNotNull(result);
    assertEquals(listDto, result);

    verify(dao, times(1)).getAll();
  }

  public void should_supports() {
    Class delimiter = mock(Class.class);
    when(delimiter.isAssignableFrom(any())).thenReturn(true);

    boolean result = service.supports(delimiter);

    assertNotNull(result);
    assertTrue(result);

    verify(delimiter, times(1)).isAssignableFrom(any());
  }

  public void should_not_supports() {
    Class delimiter = mock(Class.class);
    when(delimiter.isAssignableFrom(any())).thenReturn(false);

    boolean result = service.supports(delimiter);

    assertNotNull(result);
    assertFalse(result);

    verify(delimiter, times(1)).isAssignableFrom(any());
  }

  public static class TestGenericReadOnlyServiceImpl extends GenericReadOnlyServiceImpl<DTO, ENTITY> {
    public TestGenericReadOnlyServiceImpl(ReadOnlyDao<ENTITY> dao, DTOMapper<ENTITY, DTO> mapper) {
      super(dao, mapper);
    }
  }

  public static class ENTITY extends AbstractEntity {
    public ENTITY() {
    }

    public ENTITY(Long id) {
      this.setId(id);
    }
  }

  public static class DTO extends AbstractDTO {
    public DTO() {
    }

    public DTO(Long id) {
      this.setId(id);
    }
  }
}
