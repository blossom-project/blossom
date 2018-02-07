package com.blossom_project.core.association_user_group;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.blossom_project.core.group.GroupDTO;
import com.blossom_project.core.user.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserGroupDTOTest {

  @Test
  public void should_contains_user_and_group() throws Exception {
    AssociationUserGroupDTO association = new AssociationUserGroupDTO();
    association.setA(new UserDTO());
    association.setB(new GroupDTO());

    assertNotNull(association.getA());
    assertNotNull(association.getB());
  }

  @Test
  public void should_serialize_fields_as_user_and_group() throws Exception {
    AssociationUserGroupDTO association = new AssociationUserGroupDTO();
    association.setA(new UserDTO());
    association.setB(new GroupDTO());

    ObjectMapper objectMapper = new ObjectMapper();
    String result = objectMapper.writeValueAsString(association);
    assertTrue(result.contains("user"));
    assertTrue(result.contains("group"));
  }


}
