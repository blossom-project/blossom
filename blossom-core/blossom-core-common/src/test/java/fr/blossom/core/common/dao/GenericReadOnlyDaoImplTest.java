package fr.blossom.core.common.dao;

import com.google.common.collect.Lists;
import fr.blossom.core.common.entity.AbstractEntity;
import fr.blossom.core.common.repository.CrudRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.provider.PersistenceProvider;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({PersistenceProvider.class})
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
    public void should_set_entity_manager_null() {
        thrown.expect(IllegalArgumentException.class);
        this.dao.setEntityManager(null);
    }

    @Test
    @Ignore
    public void should_set_entity_manager() {
        mockStatic(PersistenceProvider.class);
        EntityManager entityManager = mock(EntityManager.class);
        given(PersistenceProvider.fromEntityManager(entityManager))
                .willReturn(mock(PersistenceProvider.class));
        this.dao.setEntityManager(entityManager);
    }

    @Test
    public void should_validate_not_prepare() {
        thrown.expect(IllegalStateException.class);
        this.dao.validate();
    }

    @Test
    @Ignore
    public void should_validate() {
        mockStatic(PersistenceProvider.class);
        EntityManager entityManager = mock(EntityManager.class);
        given(PersistenceProvider.fromEntityManager(entityManager)).willReturn(mock(PersistenceProvider.class));
        this.dao.setEntityManager(entityManager);
        this.dao.validate();
    }

    @Test
    @Ignore
    public void should_get_querydsl() {
        mockStatic(PersistenceProvider.class);
        EntityManager entityManager = mock(EntityManager.class);
        given(PersistenceProvider.fromEntityManager(entityManager)).willReturn(mock(PersistenceProvider.class));
        this.dao.setEntityManager(entityManager);
        this.dao.validate();

        assertNotNull(this.dao.getQuerydsl());
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
        when(this.repository.findById(any(Long.class))).thenReturn(Optional.of(new ENTITY()));
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
        Pageable pageable = PageRequest.of(0, 20);
        when(this.repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Lists.newArrayList()));
        Page<ENTITY> page = this.dao.getAll(pageable);
        assertNotNull(page);
    }

    @Test
    public void should_get_all_by_ids() {
        this.dao.getAll(Lists.newArrayList(1L, 2L));
    }

    @Test
    public void should_get_all_by_ids_empty_list() {
        List<ENTITY> entities = this.dao.getAll(Lists.newArrayList());
        assertNotNull(entities);
        assertTrue(entities.isEmpty());

    }

    public static class TestGenericReadOnlyDaoImpl extends GenericReadOnlyDaoImpl<ENTITY> {

        public TestGenericReadOnlyDaoImpl(CrudRepository<ENTITY> repository) {
            super(repository);
        }
    }

    public static class ENTITY extends AbstractEntity {

    }
}
