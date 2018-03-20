package com.blossomproject.core.common.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.plugin.core.PluginRegistry;

import com.blossomproject.core.common.dao.CrudDao;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.entity.AbstractEntity;
import com.blossomproject.core.common.mapper.DTOMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class GenericCrudServiceImplTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  CrudDao dao;

  @Mock
  DTOMapper mapper;

  @Mock
  ApplicationEventPublisher publisher;

  @Mock
  private PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationRegistry;

  @InjectMocks
  @Spy
  TestGenericCrudServiceImpl service;

  @Test
  public void should_create_with_success() {
    ENTITY entity = new ENTITY();
    entity.setId(1L);

    DTO dto = new DTO();
    dto.setId(1L);

    when(mapper.mapDto(eq(dto))).thenReturn(entity);

    when(dao.create(eq(entity))).thenReturn(entity);
    when(mapper.mapEntity(eq(entity))).thenReturn(dto);

    DTO dtoResult = service.create(dto);

    assertNotNull(dtoResult);
    assertEquals(Long.valueOf(1), dtoResult.getId());

    verify(mapper, times(1)).mapDto(any());
    verify(mapper, times(1)).mapEntity(any());
    verify(dao, times(1)).create((ENTITY) any());
    verify(publisher, times(1)).publishEvent(any());
  }

  @Test
  public void should_create_with_null_dto() {
    when(mapper.mapDto(eq(null))).thenReturn(null);
    when(dao.create(eq((ENTITY) null))).thenThrow(new InvalidDataAccessApiUsageException("error"));

    thrown.expect(InvalidDataAccessApiUsageException.class);
    service.create((DTO) null);
  }

  @Test
  public void should_return_associations_without_plugin_for_dto() {
    DTO dto = new DTO();
    dto.setId(1L);

    when(associationRegistry.hasPluginFor(eq(dto.getClass()))).thenReturn(false);

    Map<Class<? extends AbstractDTO>, Long> associations = service.associations(dto);
    assertNotNull(associations);
    assertTrue(associations.isEmpty());

    verify(associationRegistry, times(1)).hasPluginFor(any());
  }

  @Test
  public void should_return_associations_with_one_plugin_associate_with_bclass() {
    DTO dto = new DTO();
    dto.setId(1L);

    AssociationServicePlugin associationServicePlugin = mock(AssociationServicePlugin.class);
    when(associationRegistry.getPluginsFor(eq(dto.getClass())))
        .thenReturn(Lists.newArrayList(associationServicePlugin));

    when(associationServicePlugin.getAClass()).thenReturn(AbstractDTO.class);

    AbstractDTO bClass = mock(DTO.class);
    when(associationServicePlugin.getBClass()).thenReturn((Class<AbstractDTO>) bClass.getClass());

    List list = mock(List.class);
    when(list.size()).thenReturn(1);
    when(associationServicePlugin.getAssociations(eq(dto))).thenReturn(list);

    when(associationRegistry.hasPluginFor(eq(dto.getClass()))).thenReturn(true);

    Map<Class<? extends AbstractDTO>, Long> associations = service.associations(dto);
    assertNotNull(associations);
    assertTrue(associations.containsKey(bClass.getClass()));
    assertEquals(Long.valueOf(1), associations.get(bClass.getClass()));

    verify(associationRegistry, times(1)).hasPluginFor(any());
    verify(associationServicePlugin, times(1)).getAClass();
    verify(associationServicePlugin, times(1)).getBClass();
  }

  @Test
  public void should_return_associations_with_one_plugin_associate_with_aclass() {
    DTO dto = new DTO();
    dto.setId(1L);

    AssociationServicePlugin associationServicePlugin = mock(AssociationServicePlugin.class);
    when(associationRegistry.getPluginsFor(eq(dto.getClass())))
        .thenReturn(Lists.newArrayList(associationServicePlugin));

    when(associationServicePlugin.getBClass()).thenReturn(AbstractDTO.class);

    AbstractDTO aClass = mock(DTO.class);
    when(associationServicePlugin.getAClass()).thenReturn((Class<AbstractDTO>) aClass.getClass());

    List list = mock(List.class);
    when(list.size()).thenReturn(1);
    when(associationServicePlugin.getAssociations(eq(dto))).thenReturn(list);

    when(associationRegistry.hasPluginFor(eq(dto.getClass()))).thenReturn(true);

    Map<Class<? extends AbstractDTO>, Long> associations = service.associations(dto);
    assertNotNull(associations);
    assertTrue(associations.containsKey(aClass.getClass()));
    assertEquals(Long.valueOf(1), associations.get(aClass.getClass()));

    verify(associationRegistry, times(1)).hasPluginFor(any());
    verify(associationServicePlugin, times(1)).getAClass();
    verify(associationServicePlugin, times(1)).getBClass();
  }

  @Test
  public void should_return_associations_with_one_plugin_without_real_association() {
    DTO dto = new DTO();
    dto.setId(1L);

    AssociationServicePlugin associationServicePlugin = mock(AssociationServicePlugin.class);
    when(associationRegistry.getPluginsFor(eq(dto.getClass())))
        .thenReturn(Lists.newArrayList(associationServicePlugin));

    when(associationServicePlugin.getAClass()).thenReturn(AbstractDTO.class);

    AbstractDTO bClass = mock(DTO.class);
    when(associationServicePlugin.getBClass()).thenReturn((Class<AbstractDTO>) bClass.getClass());

    List list = mock(List.class);
    when(list.size()).thenReturn(0);
    when(associationServicePlugin.getAssociations(eq(dto))).thenReturn(list);

    when(associationRegistry.hasPluginFor(eq(dto.getClass()))).thenReturn(true);

    Map<Class<? extends AbstractDTO>, Long> associations = service.associations(dto);
    assertNotNull(associations);
    assertTrue(associations.isEmpty());

    verify(associationRegistry, times(1)).hasPluginFor(any());
    verify(associationServicePlugin, times(1)).getAClass();
    verify(associationServicePlugin, times(1)).getBClass();
  }

  @Test
  public void should_return_associations_with_multiple_plugin() {
    DTO dto = new DTO();
    dto.setId(1L);

    AssociationServicePlugin associationServicePlugin1 = mock(AssociationServicePlugin.class);
    AssociationServicePlugin associationServicePlugin2 = mock(AssociationServicePlugin.class);
    AssociationServicePlugin associationServicePlugin3 = mock(AssociationServicePlugin.class);

    List list = mock(List.class);
    when(list.size()).thenReturn(1);

    List<AssociationServicePlugin> servicePluginList = Lists.newArrayList(associationServicePlugin1,
        associationServicePlugin2, associationServicePlugin3);
    List<AbstractDTO> bClass = Lists.newArrayList(mock(DTO.class), mock(DTO2.class), mock(DTO3.class));

    Integer index = 0;
    for (AssociationServicePlugin plugin : servicePluginList) {
      when(plugin.getAClass()).thenReturn(AbstractDTO.class);
      when(plugin.getBClass()).thenReturn((Class<AbstractDTO>) bClass.get(index++).getClass());
      when(plugin.getAssociations(eq(dto))).thenReturn(list);
    }

    when(associationRegistry.getPluginsFor(eq(dto.getClass()))).thenReturn(servicePluginList);
    when(associationRegistry.hasPluginFor(eq(dto.getClass()))).thenReturn(true);

    Map<Class<? extends AbstractDTO>, Long> associations = service.associations(dto);
    assertNotNull(associations);

    verify(associationRegistry, times(1)).hasPluginFor(any());

    index = 0;
    for (AssociationServicePlugin plugin : servicePluginList) {
      assertTrue(associations.containsKey(bClass.get(index).getClass()));
      assertEquals(Long.valueOf(1), associations.get(bClass.get(index++).getClass()));
      verify(plugin, times(1)).getAClass();
      verify(plugin, times(1)).getBClass();
    }
  }

  @Test
  public void should_delete_with_null_entity() {
    thrown.expect(InvalidDataAccessApiUsageException.class);
    doThrow(new InvalidDataAccessApiUsageException("error")).when(service).associations(eq(null));
    this.service.delete(null);
  }

  @Test
  public void should_delete_entity_with_null_id() {
    DTO dto = new DTO();
    dto.setId(null);

    ENTITY entity = new ENTITY();
    entity.setId(null);

    doReturn(Maps.newHashMap()).when(service).associations(eq(dto));
    when(mapper.mapDto(eq(dto))).thenReturn(entity);
    doThrow(new IllegalArgumentException("error")).when(dao).delete(eq(entity));

    thrown.expect(IllegalArgumentException.class);
    this.service.delete(dto);

  }

  @Test
  public void should_delete_entity_whithout_associations() {
    DTO dto = new DTO();
    dto.setId(1L);

    ENTITY entity = new ENTITY();
    entity.setId(1L);

    doReturn(Maps.newHashMap()).when(service).associations(eq(dto));
    when(mapper.mapDto(eq(dto))).thenReturn(entity);

    Optional result = this.service.delete(dto);
    assertFalse(result.isPresent());

    verify(dao, times(1)).delete(any());
  }

  @Test
  public void should_delete_entity_whith_associations() {
    DTO dto = new DTO();
    dto.setId(1L);

    ENTITY entity = new ENTITY();
    entity.setId(1L);

    Map mapAssociation = mock(Map.class);
    when(mapAssociation.isEmpty()).thenReturn(false);

    doReturn(mapAssociation).when(service).associations(eq(dto));

    Optional result = this.service.delete(dto);
    assertTrue(result.isPresent());
    assertEquals(mapAssociation, result.get());

    verify(dao, times(0)).delete(any());
  }

  @Test
  public void should_delete_entity_whith_one_force_association_deletion() {
    DTO dto = new DTO();
    dto.setId(1L);

    ENTITY entity = new ENTITY();
    entity.setId(1L);

    Map mapAssociation = mock(Map.class);
    when(mapAssociation.isEmpty()).thenReturn(false);

    AssociationServicePlugin associationServicePlugin = mock(AssociationServicePlugin.class);
    when(associationRegistry.getPluginsFor(eq(dto.getClass())))
        .thenReturn(Lists.newArrayList(associationServicePlugin));
    when(associationRegistry.hasPluginFor(eq(dto.getClass()))).thenReturn(true);

    doReturn(mapAssociation).when(service).associations(eq(dto));

    Optional result = this.service.delete(dto, true);
    assertFalse(result.isPresent());

    verify(associationServicePlugin, times(1)).dissociateAll(any());
    verify(dao, times(1)).delete(any());
  }

  @Test
  public void should_delete_entity_whith_multiple_force_association_deletion() {
    DTO dto = new DTO();
    dto.setId(1L);

    ENTITY entity = new ENTITY();
    entity.setId(1L);

    Map mapAssociation = mock(Map.class);
    when(mapAssociation.isEmpty()).thenReturn(false);

    AssociationServicePlugin associationServicePlugin = mock(AssociationServicePlugin.class);
    AssociationServicePlugin associationServicePlugin2 = mock(AssociationServicePlugin.class);
    when(associationRegistry.getPluginsFor(eq(dto.getClass())))
        .thenReturn(Lists.newArrayList(associationServicePlugin, associationServicePlugin2));
    when(associationRegistry.hasPluginFor(eq(dto.getClass()))).thenReturn(true);

    doReturn(mapAssociation).when(service).associations(eq(dto));

    Optional result = this.service.delete(dto, true);
    assertFalse(result.isPresent());

    verify(associationServicePlugin, times(1)).dissociateAll(any());
    verify(associationServicePlugin2, times(1)).dissociateAll(any());
    verify(dao, times(1)).delete(any());
  }

  @Test
  public void should_update_with_null_entity() {
    doThrow(new IllegalArgumentException("error")).when(dao).update(eq(1L), eq(null));
    thrown.expect(IllegalArgumentException.class);
    this.service.update(1L, null);
  }

  @Test
  public void should_update_with_wrong_id() {
    doThrow(new IllegalArgumentException("error")).when(dao).update(eq(1L), any(ENTITY.class));
    when(mapper.mapDto(any(DTO.class))).thenReturn(new ENTITY());
    thrown.expect(IllegalArgumentException.class);
    this.service.update(1L, new DTO());
  }

  @Test
  public void should_update_with_wrong_entity_id() {
    DTO dto = new DTO();
    dto.setId(2L);

    ENTITY entity = new ENTITY();
    entity.setId(2L);

    doThrow(new IllegalArgumentException("error")).when(dao).update(eq(1L), eq(entity));
    when(mapper.mapDto(eq(dto))).thenReturn(entity);
    thrown.expect(IllegalArgumentException.class);
    this.service.update(1L, dto);
  }

  @Test
  public void should_update_with_success() {
    DTO dto = new DTO();
    dto.setId(2L);

    ENTITY entity = new ENTITY();
    entity.setId(2L);

    when(dao.update(eq(1L), eq(entity))).thenReturn(entity);
    when(mapper.mapDto(eq(dto))).thenReturn(entity);
    when(mapper.mapEntity(eq(entity))).thenReturn(dto);

    DTO dtoResult = this.service.update(1L, dto);

    assertNotNull(dtoResult);
    assertEquals(dto, dtoResult);

    verify(dao, times(1)).update(anyLong(), any());
  }

  @Test
  public void should_create_list_with_null_collection() {
    doThrow(new IllegalArgumentException("error")).when(dao).create(any(Collection.class));

    thrown.expect(IllegalArgumentException.class);
    this.service.create((Collection<DTO>) null);
  }

  @Test
  public void should_create_list_with_empty_collection() {
    List<DTO> toCreate = Lists.newArrayList();
    List<DTO> saved = this.service.create(toCreate);
    verify(this.dao, times(1)).create(eq(toCreate));
    assertTrue(saved.isEmpty());
  }

  @Test
  public void should_create_list_with_collection() {
    List<DTO> toCreateDto = Lists.newArrayList(new DTO(), new DTO());
    List<ENTITY> toCreateEntity = Lists.newArrayList(new ENTITY(), new ENTITY());

    when(mapper.mapDtos(eq(toCreateDto))).thenReturn(toCreateEntity);
    when(dao.create(eq(toCreateEntity))).thenReturn(toCreateEntity);
    when(mapper.mapEntities(eq(toCreateEntity))).thenReturn(toCreateDto);

    List<DTO> saved = this.service.create(toCreateDto);

    verify(this.dao, times(1)).create(eq(toCreateEntity));
    assertFalse(saved.isEmpty());
    assertEquals(saved, toCreateDto);
  }

  @Test
  public void should_update_collection_with_null() {
    doReturn(Lists.newArrayList()).when(dao).update(Maps.newHashMap());

    List saved = this.service.update(null);
    verify(this.dao, times(1)).update(any());
    assertTrue(saved.isEmpty());
  }

  @Test
  public void should_update_collection_with_empty() {
    doReturn(Lists.newArrayList()).when(dao).update(Maps.newHashMap());

    List toUpdates = Lists.newArrayList();

    when(mapper.mapDtos(eq(toUpdates))).thenReturn(Lists.newArrayList());
    List saved = this.service.update(toUpdates);
    verify(this.dao, times(1)).update(any());
    assertTrue(saved.isEmpty());
  }

  @Test
  public void should_update_collection_with_missing_db_entities() {
    List<DTO> toUpdates = Lists.newArrayList(new DTO(1L), new DTO(2L), new DTO(3L));

    doReturn(Lists.newArrayList(new ENTITY(1L), new ENTITY(3L))).when(dao).update(any(Map.class));

    List ResultEntities = Lists.newArrayList(new ENTITY(1L), new ENTITY(3L));
    when(mapper.mapDtos(eq(toUpdates))).thenReturn(Lists.newArrayList(new ENTITY(1L), new ENTITY(2L), new ENTITY(3L)));
    when(mapper.mapEntities(eq(ResultEntities))).thenReturn(Lists.newArrayList(new DTO(1L), new DTO(3L)));
    List saved = this.service.update(toUpdates);
    verify(this.dao, times(1)).update(any());
    assertEquals(2, saved.size());
  }

  @Test
  public void should_update_collection_with_success() {
    List<DTO> toUpdates = Lists.newArrayList(new DTO(1L), new DTO(2L), new DTO(3L));

    doReturn(Lists.newArrayList(new ENTITY(1L), new ENTITY(2L), new ENTITY(3L))).when(dao).update(any(Map.class));

    List ResultEntities = Lists.newArrayList(new ENTITY(1L), new ENTITY(2L), new ENTITY(3L));
    when(mapper.mapDtos(eq(toUpdates))).thenReturn(Lists.newArrayList(new ENTITY(1L), new ENTITY(2L), new ENTITY(3L)));
    when(mapper.mapEntities(eq(ResultEntities))).thenReturn(Lists.newArrayList(new DTO(1L), new DTO(2L), new DTO(3L)));
    List saved = this.service.update(toUpdates);
    verify(this.dao, times(1)).update(any());
    assertEquals(3, saved.size());
  }

  public static class TestGenericCrudServiceImpl extends GenericCrudServiceImpl<DTO, ENTITY> {

    public TestGenericCrudServiceImpl(CrudDao dao, DTOMapper mapper, ApplicationEventPublisher publisher,
        PluginRegistry associationRegistry) {
      super(dao, mapper, publisher, associationRegistry);
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

  public static class DTO2 extends AbstractDTO {
    public DTO2() {
    }
  }

  public static class DTO3 extends AbstractDTO {
    public DTO3() {
    }
  }
}
