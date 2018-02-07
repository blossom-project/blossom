package com.blossom_project.core.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.blossom_project.core.common.entity.AbstractAssociationEntity;
import com.blossom_project.core.common.entity.AbstractEntity;
import com.blossom_project.core.common.repository.AssociationRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;


@RunWith(MockitoJUnitRunner.class)
public class GenericAssociationDaoImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private AssociationRepository<A, B, ASSOCIATION> repository;
  private TestAssociationDaoImpl dao;

  @Before
  public void setUp() throws Exception {
    this.repository = mock(AssociationRepository.class);
    this.dao = new TestAssociationDaoImpl(repository);
  }

  @Test
  public void should_throw_nullpointerexception_on_empty_repository() throws Exception {
    thrown.expect(NullPointerException.class);
    this.dao = new TestAssociationDaoImpl(null);
  }


  @Test
  public void should_create() throws Exception {
    AbstractAssociationEntity<A, B> association = this.dao.create();
    assertTrue(association instanceof ASSOCIATION);
  }

  @Test
  public void should_associate_null_a() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.dao.associate(null, new B());
  }

  @Test
  public void should_associate_null_b() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.dao.associate(new A(), null);
  }

  @Test
  public void should_associate_nulls() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.dao.associate(null, null);
  }

  @Test
  public void should_associate() throws Exception {
    A a = new A();
    B b = new B();

    when(this.repository.findOneByAAndB(eq(a), eq(b))).thenReturn(null);
    when(this.repository.save(any(ASSOCIATION.class))).thenAnswer(arg -> {
      ASSOCIATION association = arg.getArgument(0);
      association.setId(1L);
      association.setCreationDate(new Date());
      association.setCreationUser("test");
      association.setModificationDate(new Date());
      association.setModificationUser("test");
      return association;
    });

    ASSOCIATION association = this.dao.associate(a, b);

    assertEquals(a, association.getA());
    assertEquals(b, association.getB());
    verify(this.repository, times(1)).save(any(ASSOCIATION.class));
  }

  @Test
  public void should_associate_already_existing() throws Exception {
    A a = new A();
    B b = new B();

    when(this.repository.findOneByAAndB(eq(a), eq(b))).thenAnswer(arg -> {
      ASSOCIATION association = new ASSOCIATION();
      association.setA(arg.getArgument(0));
      association.setB(arg.getArgument(1));
      association.setId(1L);
      association.setCreationDate(new Date());
      association.setCreationUser("test");
      association.setModificationDate(new Date());
      association.setModificationUser("test");
      return association;
    });

    ASSOCIATION association = this.dao.associate(a, b);

    assertEquals(a, association.getA());
    assertEquals(b, association.getB());
    verify(this.repository, times(0)).save(any(ASSOCIATION.class));
  }

  @Test
  public void should_dissociate_null_a() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.dao.dissociate(null, new B());
  }


  @Test
  public void should_dissociate_null_b() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.dao.dissociate(new A(), null);
  }

  @Test
  public void should_dissociate_nulls() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.dao.dissociate(null, null);
  }

  @Test
  public void should_dissociate_not_existing() throws Exception {
    A a = new A();
    B b = new B();
    this.dao.dissociate(a, b);
    verify(this.repository, times(0)).delete(any(ASSOCIATION.class));
  }


  @Test
  public void should_dissociate() throws Exception {
    A a = new A();
    B b = new B();
    when(this.repository.findOneByAAndB(eq(a), eq(b))).thenAnswer(arg -> {
      ASSOCIATION association = new ASSOCIATION();
      association.setA(arg.getArgument(0));
      association.setB(arg.getArgument(1));
      association.setId(1L);
      association.setCreationDate(new Date());
      association.setCreationUser("test");
      association.setModificationDate(new Date());
      association.setModificationUser("test");
      return association;
    });

    this.dao.dissociate(a, b);
    verify(this.repository, times(1)).delete(any(ASSOCIATION.class));
  }

  @Test
  public void should_get_by_id_not_found() {
    this.dao.getOne(1L);
    verify(this.repository, times(1)).findById(1L);
  }

  @Test
  public void should_get_by_id() {
    Long id = 1L;
    when(this.repository.findById(eq(id))).thenReturn(Optional.of(new ASSOCIATION()));
    ASSOCIATION association = this.dao.getOne(id);
    assertNotNull(association);
    verify(this.repository, times(1)).findById(id);
  }

  @Test
  public void should_getAllA_null_b() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.getAllA(null);
  }

  @Test
  public void should_getAllA() {
    when(this.repository.findAllByB(any(B.class)))
      .thenReturn(Lists.newArrayList(new ASSOCIATION()));
    List<ASSOCIATION> associations = this.dao.getAllA(new B());
    assertTrue(!associations.isEmpty());
    assertTrue(associations.size() == 1);
  }

  @Test
  public void should_getAllB_null_A() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.getAllB(null);
  }

  @Test
  public void should_getAllB() {
    when(this.repository.findAllByA(any(A.class)))
      .thenReturn(Lists.newArrayList(new ASSOCIATION()));
    List<ASSOCIATION> associations = this.dao.getAllB(new A());
    assertTrue(!associations.isEmpty());
    assertTrue(associations.size() == 1);
  }

  @Test
  public void should_getOne_null_a() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.getOne(null, new B());
  }

  @Test
  public void should_getOne_null_b() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.getOne(new A(), null);
  }

  @Test
  public void should_getOne_nulls() {
    thrown.expect(IllegalArgumentException.class);
    this.dao.getOne(null, new B());
  }

  @Test
  public void should_getOne() {
    A a = new A();
    B b = new B();
    when(this.repository.findOneByAAndB(eq(a), eq(b))).thenAnswer(arg -> {
      ASSOCIATION association = new ASSOCIATION();
      association.setA(arg.getArgument(0));
      association.setB(arg.getArgument(1));
      association.setId(1L);
      association.setCreationDate(new Date());
      association.setCreationUser("test");
      association.setModificationDate(new Date());
      association.setModificationUser("test");
      return association;
    });

    ASSOCIATION association = this.dao.getOne(a, b);
    assertNotNull(association);
  }

  public static class TestAssociationDaoImpl extends GenericAssociationDaoImpl<A, B, ASSOCIATION> {

    protected TestAssociationDaoImpl(AssociationRepository<A, B, ASSOCIATION> repository) {
      super(repository);
    }

    @Override
    protected ASSOCIATION create() {
      return new ASSOCIATION();
    }
  }

  public static class A extends AbstractEntity {

  }

  public static class B extends AbstractEntity {

  }

  public static class ASSOCIATION extends AbstractAssociationEntity<A, B> {

    private A a;
    private B b;

    @Override
    public A getA() {
      return a;
    }

    @Override
    public void setA(A a) {
      this.a = a;
    }

    @Override
    public B getB() {
      return b;
    }

    @Override
    public void setB(B b) {
      this.b = b;
    }
  }
}
