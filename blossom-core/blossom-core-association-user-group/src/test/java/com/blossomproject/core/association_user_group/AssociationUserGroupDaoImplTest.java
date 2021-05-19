package com.blossomproject.core.association_user_group;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

import com.blossomproject.core.group.GroupRepository;
import com.blossomproject.core.user.UserRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserGroupDaoImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_create_fail_on_null_repository() {
    thrown.expect(NullPointerException.class);
    new AssociationUserGroupDaoImpl(null,null,null);
  }

  @Test
  public void should_create_with_repository() {
    new AssociationUserGroupDaoImpl(mock(UserRepository.class), mock(GroupRepository.class), mock(AssociationUserGroupRepository.class));
  }

  @Test
  public void should_create_new_association() {
    AssociationUserGroupDaoImpl dao =new AssociationUserGroupDaoImpl(mock(UserRepository.class), mock(GroupRepository.class), mock(AssociationUserGroupRepository.class));
    AssociationUserGroup association = dao.create();
    assertNotNull(association);
    assertTrue(association instanceof  AssociationUserGroup);
  }
}
