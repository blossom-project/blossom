package com.blossomproject.core.common.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AbstractDTOTest {

  public static class TestDTO extends AbstractDTO {
    public TestDTO() {
    }

    public TestDTO(TestDTO dto) {
      super(dto);
    }
  }

  @Test
  public void should_copy_be_immutable() {
    TestDTO aDTO = new TestDTO();
    aDTO.setId(123L);
    aDTO.setCreationDate(Date.from(Instant.now()));
    aDTO.setCreationUser("Creation user");
    aDTO.setModificationDate(Date.from(Instant.now()));
    aDTO.setModificationUser("Modification user");

    TestDTO anotherDTO = new TestDTO(aDTO);

    assertEquals("Id should be the same for the copy", aDTO.getId(), anotherDTO.getId());
    assertEquals("Creation date should be the same for the copy", aDTO.getCreationDate(), anotherDTO.getCreationDate());
    assertEquals("Creation user should be the same for the copy", aDTO.getCreationUser(), anotherDTO.getCreationUser());
    assertEquals("Modification date should be the same for the copy", aDTO.getModificationDate(), anotherDTO.getModificationDate());
    assertEquals("Modification user should be the same for the copy", aDTO.getModificationUser(), anotherDTO.getModificationUser());

    assertNotSame("Creation date object should be distinct", aDTO.getCreationDate(), anotherDTO.getCreationDate());
    assertNotSame("Modification date object should be distinct", aDTO.getModificationDate(), anotherDTO.getModificationDate());

    aDTO.setId(456L);
    aDTO.setCreationDate(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)));
    aDTO.setCreationUser("Another Creation user");
    aDTO.setModificationDate(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)));
    aDTO.setModificationUser("Another Modification user");

    assertNotEquals("Id should be independent for the copy", aDTO.getId(), anotherDTO.getId());
    assertNotEquals("Creation date should be independent for the copy", aDTO.getCreationDate(), anotherDTO.getCreationDate());
    assertNotEquals("Creation user should be independent for the copy", aDTO.getCreationUser(), anotherDTO.getCreationUser());
    assertNotEquals("Modification date should be independent for the copy", aDTO.getModificationDate(), anotherDTO.getModificationDate());
    assertNotEquals("Modification user should be independent for the copy", aDTO.getModificationUser(), anotherDTO.getModificationUser());

  }

  @Test
  public void should_be_able_to_copy_empty_dto() {
    TestDTO aDTO = new TestDTO();
    TestDTO anotherDTO = new TestDTO(aDTO);

    assertEquals("Id should be the same for the copy", aDTO.getId(), anotherDTO.getId());
    assertEquals("Creation date should be the same for the copy", aDTO.getCreationDate(), anotherDTO.getCreationDate());
    assertEquals("Creation user should be the same for the copy", aDTO.getCreationUser(), anotherDTO.getCreationUser());
    assertEquals("Modification date should be the same for the copy", aDTO.getModificationDate(), anotherDTO.getModificationDate());
    assertEquals("Modification user should be the same for the copy", aDTO.getModificationUser(), anotherDTO.getModificationUser());
  }

}