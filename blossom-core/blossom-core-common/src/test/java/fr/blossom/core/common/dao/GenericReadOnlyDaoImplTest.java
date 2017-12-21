package fr.blossom.core.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import fr.blossom.core.common.entity.AbstractEntity;
import fr.blossom.core.common.repository.CrudRepository;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class GenericReadOnlyDaoImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private CrudRepository<ENTITY> repository;
  private TestGenericReadOnlyDaoImpl dao;

  @Before
  public void setUp() throws Exception {
    this.repository = mock(CrudRepository.class);
    this.dao = new TestGenericReadOnlyDaoImpl(this.repository);
  }

  @Test
  public void should_throw_nullpointer_exception_on_empty_repository() {
    thrown.expect(NullPointerException.class);
    this.dao = new TestGenericReadOnlyDaoImpl(null);
  }

  @Test
  public void should_get_one_not_found() {
    ENTITY entity = this.dao.getOne(1L);
    assertNull(entity);
  }

  @Test
  public void should_get_one_found() {
    when(this.repository.findOne(any(Long.class))).thenReturn(new ENTITY());
    ENTITY entity = this.dao.getOne(1L);
    assertNotNull(entity);
  }

  @Test
  public void should_get_all() {
    List<ENTITY> entities = this.dao.getAll();
    assertNotNull(entities);
  }

  @Test
  public void should_get_page_null_pageable() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.getAll((Pageable) null);
  }

  @Test
  public void should_get_page() {
    Pageable pageable = new PageRequest(0, 20);
    when(this.repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Lists.newArrayList()));
    Page<ENTITY> page = this.dao.getAll(pageable);
    assertNotNull(page);
  }

  @Test
  public void should_get_all_by_ids() {
    this.dao.getAll(Lists.newArrayList(1L, 2L));
  }

  @Test
  public void should_get_all_by_ids_empty_list() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.getAll(Lists.newArrayList());
  }

  private static class TestGenericReadOnlyDaoImpl extends GenericReadOnlyDaoImpl<ENTITY> {

    TestGenericReadOnlyDaoImpl(CrudRepository<ENTITY> repository) {
      super(repository);
    }
  }

  private static class ENTITY extends AbstractEntity {

  }
}
