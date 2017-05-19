package fr.mgargadennec.blossom.module.filemanager;

import fr.mgargadennec.blossom.module.filemanager.store.Folder;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface FileService {

  FileDTO upload(FileDTO newFile, Folder folder, InputStream content);

  void delete(FileDTO fileDTO);

  Folder folderTree();

  Folder folder(String path);

  List<FileDTO> getAll(Folder folder);

}
