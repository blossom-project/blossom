package fr.mgargadennec.blossom.module.filemanager.store;

import java.util.Collection;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
public class DiskFolder implements Folder {
  private String name;
  private String path;
  private Collection<DiskFolder> children;

  public void setName(String name) {
    this.name = name;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setChildren(Collection<DiskFolder> children) {
    this.children = children;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public Collection<? extends Folder> getChildren() {
    return children;
  }
}
