package com.blossomproject.core.common.utils.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TreeNode<T> {

  private final String id;
  private final String text;
  private T data;
  private List<TreeNode<T>> children;
  private @JsonIgnore
  TreeNode<T> parent;

  public TreeNode(String id, String text) {
    this(id, text, null);
  }

  public TreeNode(String id, String text, T data) {
    this.id = id;
    this.text = text;
    this.data = data;
    this.children = Lists.newArrayList();
  }

  public TreeNode<T> addChild(TreeNode<T> childNode) {
    childNode.parent = this;
    this.children.add(childNode);
    return childNode;
  }

  public Optional<TreeNode<T>> findChildWithId(String id) {
    return this.children.stream().filter(c -> c.id.equals(id)).findAny();
  }

  public void walkTree(TreeNodeCallback<T> callbackHandler) {
    Preconditions.checkArgument(callbackHandler != null);
    callbackHandler.handleTreeNode(this);
    children.sort(Comparator.comparing(o -> o.text));
    for (TreeNode<T> localizationTreeNode : children) {
      TreeNode<T> child = localizationTreeNode;
      child.walkTree(callbackHandler);
    }
  }

  public interface TreeNodeCallback<T> {

    void handleTreeNode(TreeNode<T> node);
  }

  public String getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public List<TreeNode<T>> getChildren() {
    return children;
  }

  public void setChildren(
    List<TreeNode<T>> children) {
    this.children = children;
  }

  public TreeNode<T> getParent() {
    return parent;
  }

}
