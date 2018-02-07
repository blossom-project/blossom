package com.blossom_project.core.common.utils.privilege;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.List;

public class SimplePrivilege implements Privilege {

  private final String namespace;
  private final String feature;
  private final String right;
  private final String privilege;

  public SimplePrivilege(String namespace, String feature, String right) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(namespace));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(feature));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(right));
    this.namespace = namespace;
    this.feature = feature;
    this.right = right;
    this.privilege = namespace + ":" + feature + ":" + right;
  }

  public SimplePrivilege(String privilege) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(privilege));
    List<String> splitted = Splitter.on(":").splitToList(privilege);
    Preconditions.checkState(splitted.size() == 3);
    this.namespace = splitted.get(0);
    this.feature = splitted.get(1);
    this.right = splitted.get(2);
    this.privilege = privilege;
  }

  @Override
  public String namespace() {
    return this.namespace;
  }

  @Override
  public String feature() {
    return this.feature;
  }

  @Override
  public String right() {
    return this.right;
  }

  @Override
  public String privilege() {
    return this.privilege;
  }

  @Override
  public boolean supports(String privilege) {
    return this.privilege.equals(privilege);
  }
}
