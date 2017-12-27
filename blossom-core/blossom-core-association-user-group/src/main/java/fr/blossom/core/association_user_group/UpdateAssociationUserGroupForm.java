package fr.blossom.core.association_user_group;

import com.google.common.collect.Sets;
import java.util.Set;

public class UpdateAssociationUserGroupForm {

  private Set<Long> ids = Sets.newHashSet();

  public Set<Long> getIds() {
    return ids;
  }

  public void setIds(Set<Long> ids) {
    this.ids = ids;
  }
}
