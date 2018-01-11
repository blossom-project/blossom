package fr.blossom.core.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.blossom.core.common.entity.AbstractEntity;
import fr.blossom.core.common.repository.CrudRepository;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GenericCrudDaoImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private CrudRepository<ENTITY> repository;
  private TestGenericCrudDaoImpl dao;

  @Before
  public void setUp() throws Exception {
    this.repository = mock(CrudRepository.class);
    this.dao = spy(new TestGenericCrudDaoImpl(repository));
  }

  @Test
  public void should_throw_nullpointer_exception_on_empty_repository() {
    thrown.expect(NullPointerException.class);
    new TestGenericCrudDaoImpl(null);
  }

  @Test
  public void should_create_with_success() {
    when(this.repository.save(any(ENTITY.class))).then(args -> {
      ENTITY entity = args.getArgument(0);
      entity.setId(1L);
      entity.setCreationDate(new Date(System.currentTimeMillis()));
      entity.setModificationDate(new Date(System.currentTimeMillis()));
      entity.setCreationUser("test");
      entity.setModificationUser("test");
      return entity;
    });

    ENTITY createdEntity = this.dao.create(new ENTITY());

    assertNotNull(createdEntity);
    assertNotNull(createdEntity.getId());
    assertEquals(Long.valueOf(1), createdEntity.getId());
  }

  @Test
  public void should_update_with_success() {
    ENTITY toUpdate = new ENTITY();
    toUpdate.setId(1L);
    toUpdate.setCreationDate(new Date(System.currentTimeMillis()));
    toUpdate.setModificationDate(new Date(System.currentTimeMillis()));
    toUpdate.setCreationUser("test");
    toUpdate.setModificationUser("test");

    when(this.repository.findById(anyLong())).thenReturn(Optional.of(toUpdate));
    when(this.repository.save(any(ENTITY.class))).thenReturn(toUpdate);

    ENTITY createdEntity = this.dao.update(1L, toUpdate);

    assertNotNull(createdEntity);
    assertNotNull(createdEntity.getId());
    assertEquals(Long.valueOf(1), createdEntity.getId());
  }

  @Test
  public void should_update_with_wrong_id() {
    thrown.expect(IllegalArgumentException.class);
    when(this.repository.findById(anyLong())).thenReturn(Optional.empty());
    ENTITY updatedEntity = this.dao.update(1L, new ENTITY());
  }

  @Test
  public void should_update_with_wrong_entity_id() {
    thrown.expect(IllegalArgumentException.class);
    Long id = 1L;
    when(this.repository.findById(anyLong())).then(arg -> {
      ENTITY entity = new ENTITY();
      entity.setId(id);
      return Optional.of(entity);
    });

    ENTITY entity = new ENTITY();
    entity.setId(2L);

    ENTITY saved = this.dao.update(1L, entity);
  }

  @Test
  public void should_update_with_null_entity() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.update(1L, null);
  }


  @Test
  public void should_delete_with_null_entity() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.delete(null);
  }

  @Test
  public void should_delete_with_entity_with_null_id() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.delete(new ENTITY());
  }

  @Test
  public void should_delete() {
    ENTITY entity = new ENTITY();
    entity.setId(1L);

    doNothing().when(this.repository).deleteById(anyLong());
    this.dao.delete(entity);
  }

  @Test
  public void should_create_list_with_null_collection() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.create((Collection<ENTITY>) null);
  }

  @Test
  public void should_create_list_with_empty_collection() {
    List<ENTITY> toCreate = Lists.newArrayList();
    List<ENTITY> saved = this.dao.create(toCreate);
    verify(this.repository, times(0)).saveAll(eq(toCreate));
    assertTrue(saved.isEmpty());
  }

  @Test
  public void should_create_list_with_collection() {
    List<ENTITY> toCreate = Lists.newArrayList(new ENTITY(), new ENTITY());
    when(this.repository.saveAll(eq(toCreate))).thenAnswer(arg -> {
      List<ENTITY> initial = (List<ENTITY>) arg.getArgument(0);
      final AtomicLong i = new AtomicLong(0);
      initial.forEach(e -> e.setId(i.incrementAndGet()));
      return initial;
    });

    List<ENTITY> saved = this.dao.create(toCreate);
    verify(this.repository, times(1)).saveAll(eq(toCreate));
    assertFalse(saved.isEmpty());
    assertEquals(saved.size(), toCreate.size());
  }


  @Test
  public void should_update_map_with_null() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.update((Map<Long, ENTITY>) null);
  }

  @Test
  public void should_update_map_with_empty() {
    Map<Long, ENTITY> toUpdates = Maps.newHashMap();
    List<ENTITY> saved = this.dao.update(toUpdates);
    verify(this.repository, times(0)).save(any(ENTITY.class));
    assertTrue(saved.isEmpty());
  }

  @Test
  public void should_update_map_with_missing_db_entities() {
    Map<Long, ENTITY> toUpdates = ImmutableMap.<Long, ENTITY>builder()
      .put(1L, new ENTITY(1L))
      .put(2L, new ENTITY(2L))
      .put(3L, new ENTITY(3L))
      .build();

    List<ENTITY> found = Lists.newArrayList(toUpdates.get(1L), toUpdates.get(3L));

    when(this.repository.findAllById(eq(toUpdates.keySet()))).thenReturn(found);
    when(this.repository.saveAll(anyCollection())).thenReturn(found);

    List<ENTITY> saved = this.dao.update(toUpdates);

    verify(this.repository, times(1)).saveAll(anyCollection());
    assertFalse(saved.isEmpty());
    assertEquals(found.size(), saved.size());

  }

  public static class TestGenericCrudDaoImpl extends GenericCrudDaoImpl<ENTITY> {

    public TestGenericCrudDaoImpl(CrudRepository<ENTITY> repository) {
      super(repository);
    }

    @Override
    protected ENTITY updateEntity(ENTITY originalEntity, ENTITY modifiedEntity) {
      return originalEntity;
    }
  }

  public static class ENTITY extends AbstractEntity {

    public ENTITY() {
    }

    public ENTITY(Long id) {
      this.setId(id);
    }
  }

}
