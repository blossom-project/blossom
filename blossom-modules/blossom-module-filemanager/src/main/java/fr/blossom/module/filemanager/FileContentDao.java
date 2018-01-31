package fr.blossom.module.filemanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by Maël Gargadennnec on 22/05/2017.
 */
public interface FileContentDao {

  FileContent store(File file, InputStream data, long size) throws IOException, SQLException;

  InputStream read(Long id) throws SQLException, FileNotFoundException;
}
