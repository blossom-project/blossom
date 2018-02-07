package com.blossom_project.core.role;

import com.google.common.collect.Lists;
import java.util.List;

public class RolePrivilegeUpdateForm {

  private List<String> privileges;

  public RolePrivilegeUpdateForm() {
    this.privileges = Lists.newArrayList();
  }

  public RolePrivilegeUpdateForm(RoleDTO role) {
    this();
    if (role.getPrivileges() != null) {
      this.privileges.addAll(role.getPrivileges());
    }
  }

  public List<String> getPrivileges() {
    return privileges;
  }

  public void setPrivileges(List<String> privileges) {
    this.privileges = privileges;
  }
}
