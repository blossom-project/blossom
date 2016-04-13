package fr.mgargadennec.blossom.core.common.support.history;

public class BlossomHistoryChangeDTO {

  private String property;
  private String left;
  private String right;

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public String getLeft() {
    return left;
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public String getRight() {
    return right;
  }

  public void setRight(String right) {
    this.right = right;
  }
}
