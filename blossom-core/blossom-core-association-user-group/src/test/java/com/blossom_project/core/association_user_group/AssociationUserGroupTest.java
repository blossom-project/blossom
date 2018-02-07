package com.blossom_project.core.association_user_group;

import static org.junit.Assert.assertNotNull;

import com.blossom_project.core.group.Group;
import com.blossom_project.core.user.User;
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
