package fr.mgargadennec.blossom.module.filemanager.store;

import java.util.Collection;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
public interface Folder {
  String getName();

  String getPath();

  Collection<? extends Folder> getChildren();
}
