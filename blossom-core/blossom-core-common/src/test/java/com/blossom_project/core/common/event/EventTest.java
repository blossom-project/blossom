package com.blossom_project.core.common.event;

import static org.junit.Assert.assertEquals;

import com.blossom_project.core.common.dto.AbstractAssociationDTO;
import com.blossom_project.core.common.dto.AbstractDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class EventTest {

  @Test
  public void should_create_before_dissociated_event() throws Exception {
    TestAssociationDTO dto = new TestAssociationDTO();
    BeforeDissociatedEvent event= new BeforeDissociatedEvent(this, dto);

    assertEquals(event.getDTO(), dto);
    assertEquals(event.getSource(), this);
  }
  @Test
  public void should_create_after_dissociated_event() throws Exception {
    TestAssociationDTO dto = new TestAssociationDTO();
    AfterDissociatedEvent event= new AfterDissociatedEvent(this, dto);

    assertEquals(event.getDTO(), dto);
    assertEquals(event.getSource(), this);
  }

  @Test
  public void should_create_associated_event() throws Exception {
    TestAssociationDTO dto = new TestAssociationDTO();
    AssociatedEvent event= new AssociatedEvent(this, dto);

    assertEquals(event.getDTO(), dto);
    assertEquals(event.getSource(), this);
  }

  @Test
  public void should_create_before_deleted_event() throws Exception {
    TestDTO dto = new TestDTO();
    BeforeDeletedEvent event= new BeforeDeletedEvent(this, dto);

    assertEquals(event.getDTO(), dto);
    assertEquals(event.getSource(), this);
  }

  @Test
  public void should_create_deleted_event() throws Exception {
    TestDTO dto = new TestDTO();
    DeletedEvent event= new DeletedEvent(this, dto);

    assertEquals(event.getDTO(), dto);
    assertEquals(event.getSource(), this);
  }

  @Test
  public void should_create_created_event() throws Exception {
    TestDTO dto = new TestDTO();
    CreatedEvent event= new CreatedEvent(this, dto);

    assertEquals(event.getDTO(), dto);
    assertEquals(event.getSource(), this);
  }
  @Test
  public void should_create_updated_event() throws Exception {
    TestDTO dto = new TestDTO();
    UpdatedEvent event= new UpdatedEvent(this, dto);

    assertEquals(event.getDTO(), dto);
    assertEquals(event.getSource(), this);
  }


  private class TestAssociationDTO extends AbstractAssociationDTO{}
  private class TestDTO extends AbstractDTO{}
}
