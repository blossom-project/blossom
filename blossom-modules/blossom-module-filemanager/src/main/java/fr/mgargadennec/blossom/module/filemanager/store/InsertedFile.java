
package fr.mgargadennec.blossom.module.filemanager.store;

import java.io.Serializable;

public class InsertedFile implements Serializable {
  private final String location;

  public InsertedFile(String location) {
    this.location = location;
  }

  public String getLocation() {
    return location;
  }
}
