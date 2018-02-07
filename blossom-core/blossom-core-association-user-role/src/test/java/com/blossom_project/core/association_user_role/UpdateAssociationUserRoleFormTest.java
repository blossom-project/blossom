package com.blossom_project.core.association_user_role;

import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UpdateAssociationUserRoleFormTest {

  @Test
  public void should_set_ids() throws Exception {
    Set<Long> ids = Sets.newHashSet(1L, 2L, 3L);
    UpdateAssociationUserRoleForm form = new UpdateAssociationUserRoleForm();
    form.setIds(ids);

    assertTrue(ids.equals(form.getIds()));
  }
}
