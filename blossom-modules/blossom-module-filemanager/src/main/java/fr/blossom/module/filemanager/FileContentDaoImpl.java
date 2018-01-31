package fr.blossom.module.filemanager;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by Maël Gargadennnec on 22/05/2017.
 */
public class FileContentDaoImpl implements FileContentDao {
  private final FileContentRepository repository;

  public FileContentDaoImpl(FileContentRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public FileContent store(File file, InputStream data, long size) throws IOException, SQLException {
    FileContent fileContent = new FileContent();
    fileContent.setFileId(file.getId());
    fileContent.setData(StreamUtils.copyToByteArray(data));

    this.repository.save(fileContent);

    return fileContent;
  }

  @Override
  public InputStream read(Long fileId) throws SQLException, FileNotFoundException {
    return new ByteArrayInputStream(this.repository.findOne(QFileContent.fileContent.fileId.eq(fileId)).orElseThrow(FileNotFoundException::new).getData());
  }
}
