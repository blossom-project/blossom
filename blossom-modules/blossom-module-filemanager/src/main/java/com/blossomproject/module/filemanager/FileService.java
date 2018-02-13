package com.blossomproject.module.filemanager;

import com.blossomproject.core.common.service.CrudService;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface FileService extends CrudService<FileDTO> {

  FileDTO upload(MultipartFile multipartFile) throws SQLException, IOException;

  InputStream download(long fileId) throws SQLException, FileNotFoundException;

}
