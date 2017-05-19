package fr.mgargadennec.blossom.module.filemanager.store;

import fr.mgargadennec.blossom.module.filemanager.FileDTO;

import java.io.InputStream;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
public interface FileStore {

  InsertedFile add(FileDTO file, InputStream inputStream);

  InsertedFile replace(FileDTO file, InputStream inputStream);

  StreamedFile get(FileDTO file);

  Folder folderTree();

  Folder folder(String path);

  boolean delete(FileDTO file);
}
