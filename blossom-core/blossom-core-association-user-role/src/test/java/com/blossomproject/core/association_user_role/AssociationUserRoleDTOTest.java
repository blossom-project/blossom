package com.blossomproject.core.association_user_role;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.blossomproject.core.role.RoleDTO;
import com.blossomproject.core.user.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserRoleDTOTest {

  @Test
  public void should_contains_user_and_role() throws Exception {
    AssociationUserRoleDTO association = new AssociationUserRoleDTO();
    association.setA(new UserDTO());
    association.setB(new RoleDTO());

    assertNotNull(association.getA());
    assertNotNull(association.getB());
  }

  @Test
  public void should_serialize_fields_as_user_and_role() throws Exception {
    AssociationUserRoleDTO association = new AssociationUserRoleDTO();
    association.setA(new UserDTO());
    association.setB(new RoleDTO());

    ObjectMapper objectMapper = new ObjectMapper();
    String result = objectMapper.writeValueAsString(association);
    assertTrue(result.contains("user"));
    assertTrue(result.contains("role"));
  }


}
