package fr.mgargadennec.blossom.security.support.security.impl;

import org.springframework.security.core.GrantedAuthority;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;

public class BlossomGroupGrantedAuthority implements GrantedAuthority {

  private long groupId;

  public BlossomGroupGrantedAuthority(long id) {
    this.groupId = id;
  }

  @Override
  public String getAuthority() {
    return BlossomConst.SECURITY_GROUP_PREFIX + groupId;
  }

  public long getGroupId() {
    return groupId;
  }
}
