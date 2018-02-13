package com.blossomproject.core.association_user_group;

import static org.junit.Assert.assertNotNull;

import com.blossomproject.core.group.Group;
import com.blossomproject.core.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserGroupTest {

  @Test
  public void should_contains_user_and_group() throws Exception {
    AssociationUserGroup association = new AssociationUserGroup();
    association.setA(new User());
    association.setB(new Group());

    assertNotNull(association.getA());
    assertNotNull(association.getB());
  }

}
