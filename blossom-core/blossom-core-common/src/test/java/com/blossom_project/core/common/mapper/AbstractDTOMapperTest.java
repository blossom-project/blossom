package com.blossom_project.core.common.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.entity.AbstractEntity;
import java.util.Date;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AbstractDTOMapperTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private AbstractDTOMapper<TestEntity, TestDTO> mapper;

  @Before
  public void setUp() throws Exception {
    this.mapper = new TestAbstractDTOMapper();
  }

  @Test
  public void should_map_common_fields_to_dto() throws Exception {
    TestEntity entity = new TestEntity();
    entity.setId(1L);
    entity.setCreationDate(new Date());
    entity.setModificationDate(new Date());
    entity.setCreationUser("test");
    entity.setModificationUser("test2");

    TestDTO dto = new TestDTO();

    this.mapper.mapEntityCommonFields(dto, entity);

    assertNotNull(dto.getId());
    assertNotNull(dto.getCreationDate());
    assertNotNull(dto.getModificationDate());
    assertNotNull(dto.getCreationUser());
    assertNotNull(dto.getModificationUser());

    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getCreationDate(), entity.getCreationDate());
    assertEquals(dto.getModificationDate(), entity.getModificationDate());
    assertEquals(dto.getCreationUser(), entity.getCreationUser());
    assertEquals(dto.getModificationUser(), entity.getModificationUser());
  }


  @Test
  public void should_map_common_fields_to_dto_throws_npe_if_target_is_null() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.mapper.mapEntityCommonFields(null, new TestEntity());
  }

  @Test
  public void should_map_common_fields_to_dto_throws_npe_if_source_is_null() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.mapper.mapEntityCommonFields(new TestDTO(), null);
  }

  @Test
  public void should_map_common_fields_to_entity() throws Exception {
    TestDTO dto = new TestDTO();
    dto.setId(1L);
    dto.setCreationDate(new Date());
    dto.setModificationDate(new Date());
    dto.setCreationUser("test");
    dto.setModificationUser("test2");

    TestEntity entity = new TestEntity();

    this.mapper.mapDtoCommonFields(entity, dto);

    assertNotNull(entity.getId());
    assertNotNull(entity.getCreationDate());
    assertNotNull(entity.getModificationDate());
    assertNotNull(entity.getCreationUser());
    assertNotNull(entity.getModificationUser());

    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getCreationDate(), dto.getCreationDate());
    assertEquals(entity.getModificationDate(), dto.getModificationDate());
    assertEquals(entity.getCreationUser(), dto.getCreationUser());
    assertEquals(entity.getModificationUser(), dto.getModificationUser());
  }

  @Test
  public void should_map_common_fields_to_entity_throws_npe_if_target_is_null() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.mapper.mapDtoCommonFields(null, new TestDTO());
  }

  @Test
  public void should_map_common_fields_to_entity_throws_npe_if_source_is_null() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    this.mapper.mapDtoCommonFields(new TestEntity(), null);
  }

  @Test
  public void should_plugin_support_entity() throws Exception {
    assertTrue(this.mapper.supports(TestEntity.class));
  }


  @Test
  public void should_plugin_not_support_other_entity() throws Exception {
    assertFalse(this.mapper.supports(WrongTestEntity.class));
  }

  private class WrongTestEntity extends AbstractEntity {}


  private class TestEntity extends AbstractEntity {

    private String test;

    public String getTest() {
      return test;
    }

    public void setTest(String test) {
      this.test = test;
    }
  }

  private class TestDTO extends AbstractDTO {

    private String test;

    public String getTest() {
      return test;
    }

    public void setTest(String test) {
      this.test = test;
    }
  }

  private class TestAbstractDTOMapper extends AbstractDTOMapper<TestEntity, TestDTO> {

    @Override
    public TestDTO mapEntity(TestEntity entity) {
      TestDTO dto = new TestDTO();
      mapEntityCommonFields(dto, entity);
      dto.setTest(entity.getTest());
      return dto;
    }

    @Override
    public TestEntity mapDto(TestDTO dto) {
      TestEntity entity = new TestEntity();
      mapDtoCommonFields(entity, dto);
      entity.setTest(dto.getTest());
      return entity;
    }
  }
}
