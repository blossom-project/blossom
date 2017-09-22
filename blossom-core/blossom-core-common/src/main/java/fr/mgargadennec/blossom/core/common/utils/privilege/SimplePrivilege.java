package fr.mgargadennec.blossom.core.common.utils.privilege;

public class SimplePrivilege implements PrivilegePlugin {

  private final String namespace;
  private final String right;
  private final String privilege;

  public SimplePrivilege(String namespace, String right) {
    this.namespace = namespace;
    this.right = right;
    this.privilege = namespace + ":" + right;
  }

  public SimplePrivilege(String privilege) {
    this.namespace = privilege.split(":")[0];
    this.right = privilege.split(":")[1];
    this.privilege = privilege;
  }

  @Override
  public String namespace() {
    return this.namespace;
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
