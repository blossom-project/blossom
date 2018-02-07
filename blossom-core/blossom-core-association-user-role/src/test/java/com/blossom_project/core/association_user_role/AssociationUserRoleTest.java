package com.blossom_project.core.association_user_role;

import static org.junit.Assert.assertNotNull;

import com.blossom_project.core.role.Role;
import com.blossom_project.core.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserRoleTest {

  @Test
  public void should_contains_user_and_role() throws Exception {
    AssociationUserRole association = new AssociationUserRole();
    association.setA(new User());
    association.setB(new Role());

    assertNotNull(association.getA());
    assertNotNull(association.getB());
  }

}
