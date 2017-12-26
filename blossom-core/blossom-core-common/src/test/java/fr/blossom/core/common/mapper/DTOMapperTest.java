package fr.blossom.core.common.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Lists;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.entity.AbstractEntity;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@RunWith(MockitoJUnitRunner.class)
public class DTOMapperTest {

  private DTOMapper<TestEntity, TestDTO> mapper;

  @Before
  public void setUp() throws Exception {
    this.mapper = spy(new TestDTOMapper());
  }

  @Test
  public void should_map_entity_collection() throws Exception {
    List<TestEntity> entities = Lists.newArrayList(new TestEntity(), new TestEntity());
    List<TestDTO> dtos = this.mapper.mapEntities(entities);

    assertTrue(!dtos.isEmpty());
    assertEquals(dtos.size(), entities.size());
    verify(this.mapper, times(entities.size())).mapEntity(any(TestEntity.class));
  }

  @Test
  public void should_map_empty_entity_collection() throws Exception {
    List<TestEntity> entities = Lists.newArrayList();
    List<TestDTO> dtos = this.mapper.mapEntities(entities);

    assertNotNull(dtos);
    assertTrue(dtos.isEmpty());
    verify(this.mapper, times(0)).mapEntity(any(TestEntity.class));
  }

  @Test
  public void should_map_null_entity_collection() throws Exception {
    List<TestEntity> entities = null;
    List<TestDTO> dtos = this.mapper.mapEntities(entities);

    assertNull(dtos);
    verify(this.mapper, times(0)).mapEntity(any(TestEntity.class));
  }

  @Test
  public void should_map_dto_collection() throws Exception {
    List<TestDTO> dtos = Lists.newArrayList(new TestDTO(), new TestDTO());
    List<TestEntity> entities= this.mapper.mapDtos(dtos);

    assertTrue(!entities.isEmpty());
    assertEquals(entities.size(), dtos.size());
    verify(this.mapper, times(dtos.size())).mapDto(any(TestDTO.class));
  }

  @Test
  public void should_map_empty_dto_collection() throws Exception {
    List<TestDTO> dtos = Lists.newArrayList();
    List<TestEntity> entities = this.mapper.mapDtos(dtos);

    assertNotNull(entities);
    assertTrue(entities.isEmpty());
    verify(this.mapper, times(0)).mapDto(any(TestDTO.class));
  }

  @Test
  public void should_map_null_dto_collection() throws Exception {
    List<TestDTO> dtos = null;
    List<TestEntity> entities = this.mapper.mapDtos(dtos);

    assertNull(entities);
    verify(this.mapper, times(0)).mapDto(any(TestDTO.class));
  }



  @Test
  public void should_map_entity_page() throws Exception {
    Page<TestEntity> entities = new PageImpl<>(Lists.newArrayList(new TestEntity(), new TestEntity()));
    Page<TestDTO> dtos = this.mapper.mapEntitiesPage(entities);

    assertTrue(dtos.hasContent());
    assertEquals(dtos.getSize(), entities.getSize());
    verify(this.mapper, times(entities.getContent().size())).mapEntity(any(TestEntity.class));
  }

  @Test
  public void should_map_empty_entity_page() throws Exception {
    Page<TestEntity> entities = new PageImpl<>(Lists.newArrayList());
    Page<TestDTO> dtos = this.mapper.mapEntitiesPage(entities);

    assertNotNull(dtos);
    assertFalse(dtos.hasContent());
    verify(this.mapper, times(0)).mapEntity(any(TestEntity.class));
  }

  @Test
  public void should_map_null_entity_page() throws Exception {
    Page<TestEntity> entities = null;
    Page<TestDTO> dtos = this.mapper.mapEntitiesPage(entities);

    assertNull(dtos);
    verify(this.mapper, times(0)).mapEntity(any(TestEntity.class));
  }

  @Test
  public void should_map_dto_page() throws Exception {
    Page<TestDTO> dtos = new PageImpl<>(Lists.newArrayList(new TestDTO(), new TestDTO()));
    Page<TestEntity> entities= this.mapper.mapDtosPage(dtos);

    assertTrue(entities.hasContent());
    assertEquals(dtos.getSize(), entities.getSize());
    verify(this.mapper, times(dtos.getContent().size())).mapDto(any(TestDTO.class));
  }

  @Test
  public void should_map_empty_dto_page() throws Exception {
    Page<TestDTO> dtos = new PageImpl<>(Lists.newArrayList());
    Page<TestEntity> entities = this.mapper.mapDtosPage(dtos);

    assertNotNull(entities);
    assertFalse(entities.hasContent());
    verify(this.mapper, times(0)).mapDto(any(TestDTO.class));
  }

  @Test
  public void should_map_null_dto_page() throws Exception {
    Page<TestDTO> dtos = null;
    Page<TestEntity> entities = this.mapper.mapDtosPage(dtos);

    assertNull(entities);
    verify(this.mapper, times(0)).mapDto(any(TestDTO.class));
  }


  private class TestEntity extends AbstractEntity {

  }

  private class TestDTO extends AbstractDTO {

  }

  private class TestDTOMapper implements DTOMapper<TestEntity, TestDTO> {

    @Override
    public TestDTO mapEntity(TestEntity entity) {
      return new TestDTO();
    }

    @Override
    public TestEntity mapDto(TestDTO dto) {
      return new TestEntity();
    }

    @Override
    public boolean supports(Class<? extends AbstractEntity> delimiter) {
      return false;
    }
  }
}
