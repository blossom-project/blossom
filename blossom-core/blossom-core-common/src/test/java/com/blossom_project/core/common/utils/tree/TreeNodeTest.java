package com.blossom_project.core.common.utils.tree;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class TreeNodeTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_test_root_node_empty() throws Exception {
    TreeNode<String> treeNode = new TreeNode<String>("root", "rootnode", "test");
    assertEquals("root", treeNode.getId());
    assertEquals("rootnode", treeNode.getText());
    assertEquals("test", treeNode.getData());
  }

  @Test
  public void should_test_root_node_without_data() throws Exception {
    TreeNode<String> treeNode = new TreeNode<String>("root", "rootnode");
    assertEquals("root", treeNode.getId());
    assertEquals("rootnode", treeNode.getText());
    assertNull(treeNode.getData());
  }

  @Test
  public void should_test_root_node_set_data() throws Exception {
    TreeNode<String> treeNode = new TreeNode<String>("root", "rootnode");
    treeNode.setData("TEST");
    assertEquals("root", treeNode.getId());
    assertEquals("rootnode", treeNode.getText());
    assertEquals("TEST", treeNode.getData());
  }

  @Test
  public void should_test_root_node_with_children_data() throws Exception {
    TreeNode<String> treeNode = new TreeNode<String>("root", "rootnode");
    treeNode.addChild(new TreeNode<String>("child1", "child1"));
    treeNode.addChild(new TreeNode<String>("child2", "child2"));

    assertEquals("root", treeNode.getId());
    assertEquals("rootnode", treeNode.getText());
    assertNull(treeNode.getData());
    assertEquals(2, treeNode.getChildren().size());
    assertEquals(treeNode, treeNode.getChildren().get(0).getParent());
    assertEquals(treeNode, treeNode.getChildren().get(1).getParent());
  }

  @Test
  public void should_find_child_node() throws Exception {
    TreeNode<String> treeNode = new TreeNode<String>("root", "rootnode");
    treeNode.addChild(new TreeNode<String>("child1", "child1"));
    treeNode.addChild(new TreeNode<String>("child2", "child2"));

    Optional<TreeNode<String>> found = treeNode.findChildWithId("child2");

    assertTrue(found.isPresent());
    assertEquals("child2", found.get().getId());
    assertEquals("child2", found.get().getText());
  }

  @Test
  public void should_find_child_node_not_found() throws Exception {
    TreeNode<String> treeNode = new TreeNode<String>("root", "rootnode");
    treeNode.addChild(new TreeNode<String>("child1", "child1"));
    treeNode.addChild(new TreeNode<String>("child2", "child2"))
      .addChild(new TreeNode<String>("child21", "child21"));

    Optional<TreeNode<String>> found = treeNode.findChildWithId("child21");

    assertFalse(found.isPresent());
  }

  @Test
  public void should_replace_children() throws Exception {
    TreeNode<String> treeNode = new TreeNode<String>("root", "rootnode");
    treeNode.addChild(new TreeNode<String>("child1", "child1"));
    treeNode.addChild(new TreeNode<String>("child2", "child2"));

    treeNode.setChildren(Lists.newArrayList(new TreeNode<String>("child21", "child21")));

    Optional<TreeNode<String>> found = treeNode.findChildWithId("child21");
    assertTrue(found.isPresent());
    assertEquals("child21", found.get().getId());
    assertEquals("child21", found.get().getText());
  }

  @Test
  public void should_not_walk_tree_without_callback() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    TreeNode<String> treeNode = new TreeNode<String>("root", "rootnode");
    treeNode.walkTree(null);
  }

  @Test
  public void should_walk_tree_with_callback() throws Exception {
    TreeNode<String> treeNode = new TreeNode<String>("root", "rootnode");
    treeNode.addChild(new TreeNode<String>("child1", "child1"));
    treeNode.addChild(new TreeNode<String>("child2", "child2"));

    treeNode.walkTree(a -> {});
  }
}
