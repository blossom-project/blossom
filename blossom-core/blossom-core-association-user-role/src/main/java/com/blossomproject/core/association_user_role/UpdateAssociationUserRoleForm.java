package com.blossomproject.core.association_user_role;

import com.google.common.collect.Sets;
import java.util.Set;

public class UpdateAssociationUserRoleForm {

  private Set<Long> ids = Sets.newHashSet();

  public Set<Long> getIds() {
    return ids;
  }

  public void setIds(Set<Long> ids) {
    this.ids = ids;
  }
}
