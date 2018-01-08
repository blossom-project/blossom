package fr.blossom.core.association_user_role;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssociationUserRoleDaoImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_create_fail_on_null_repository() {
    thrown.expect(NullPointerException.class);
    new AssociationUserRoleDaoImpl(null);
  }

  @Test
  public void should_create_with_repository() {
    new AssociationUserRoleDaoImpl(mock(AssociationUserRoleRepository.class));
  }

  @Test
  public void should_create_new_association() {
    AssociationUserRoleDaoImpl dao =new AssociationUserRoleDaoImpl(mock(AssociationUserRoleRepository.class));
    AssociationUserRole association = dao.create();
    assertNotNull(association);
    assertTrue(association instanceof  AssociationUserRole);
  }
}
