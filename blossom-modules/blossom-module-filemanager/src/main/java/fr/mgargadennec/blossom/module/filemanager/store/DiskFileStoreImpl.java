package fr.mgargadennec.blossom.module.filemanager.store;

import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.module.filemanager.FileDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
public class DiskFileStoreImpl implements FileStore {
  private final Path root;

  public DiskFileStoreImpl(Path root) {
    this.root = root;
  }

  @Override
  public InsertedFile add(FileDTO file, InputStream inputStream) {
    Path directory = computeDirectory(file);
    Path path = computeFilePath(file);
    if (Files.exists(path)) {
      throw new RuntimeException("File already exists !");
    }
    try {
      Files.createDirectories(directory);
    } catch (IOException e) {
      throw new RuntimeException("Cannot create directories " + directory.toAbsolutePath());
    }

    try {
      Files.createFile(path);
    } catch (IOException e) {
      throw new RuntimeException("Cannot create file " + directory.toAbsolutePath());
    }

    try {
      Files.copy(inputStream, path);
    } catch (IOException e) {
      throw new RuntimeException("Cannot copy input stream to file path" + path.toAbsolutePath());
    }

    InsertedFile insertedFile = new InsertedFile(path.toString());
    return insertedFile;
  }

  @Override
  public InsertedFile replace(FileDTO file, InputStream inputStream) {
    Path directory = computeDirectory(file);
    Path path = computeFilePath(file);
    if (!Files.exists(path)) {
      throw new RuntimeException("File doesn't exists !");
    }
    try {
      Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("Cannot replace file with path " + path.toAbsolutePath() + " with new content");
    }

    InsertedFile insertedFile = new InsertedFile(path.toString());
    return insertedFile;
  }

  @Override
  public StreamedFile get(FileDTO file) {
    Path path = computeFilePath(file);
    if (!Files.exists(path)) {
      throw new RuntimeException("File doesn't exists !");
    }
    try {
      return new StreamedFile(Files.newInputStream(path));
    } catch (IOException e) {
      throw new RuntimeException("File cannot be read !");
    }
  }

  @Override
  public Folder folderTree() {
    DiskFolder folder = new DiskFolder();
    folder.setName("/");
    folder.setPath("");

    List<DiskFolder> children = Lists.newArrayList();
    for (java.io.File file : root.toFile().listFiles(java.io.File::isDirectory)) {
      children.add(toFolder(folder.getPath(), file));
    }
    folder.setChildren(children);
    return folder;
  }

  public Folder folder(String path) {
    Path folderPath = root.resolve(path);
    if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
      DiskFolder folder = new DiskFolder();
      folder.setName(folderPath.getName());
      folder.setPath(path);
      return folder;
    }
    return null;
  }

  @Override
  public boolean delete(FileDTO file) {
    Path path = computeFilePath(file);
    if (!Files.exists(path)) {
      throw new RuntimeException("File doesn't exists !");
    }
    try {
      return Files.deleteIfExists(path);
    } catch (IOException e) {
      throw new RuntimeException("File cannot be deleted !");
    }
  }

  private Path computeDirectory(FileDTO file) {
    return this.root.resolve(file.getPath());
  }

  private Path computeFilePath(FileDTO file) {
    return computeDirectory(file).resolve(file.getId() + "_" + file.getName() + "." + file.getExtension());
  }

  private DiskFolder toFolder(String parentPath, java.io.File directory) {
    DiskFolder folder = new DiskFolder();
    folder.setName(directory.getName());
    folder.setPath(parentPath + File.separator + directory.getName());

    List<DiskFolder> children = Lists.newArrayList();
    for (java.io.File file : directory.listFiles(java.io.File::isDirectory)) {
      children.add(toFolder(folder.getPath(), file));
    }
    folder.setChildren(children);
    return folder;
  }

}
