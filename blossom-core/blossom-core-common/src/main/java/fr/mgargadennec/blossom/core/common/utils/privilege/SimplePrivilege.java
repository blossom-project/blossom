package fr.mgargadennec.blossom.core.common.utils.privilege;

public class SimplePrivilege implements Privilege {

  private final String namespace;
  private final String feature;
  private final String right;
  private final String privilege;

  public SimplePrivilege(String namespace, String feature, String right) {
    this.namespace = namespace;
    this.feature = feature;
    this.right = right;
    this.privilege = namespace + ":" + feature + ":" + right;
  }

  public SimplePrivilege(String privilege) {
    this.namespace = privilege.split(":")[0];
    this.feature = privilege.split(":")[1];
    this.right = privilege.split(":")[2];
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
