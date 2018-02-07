package com.blossom_project.module.filemanager;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.service.AssociationServicePlugin;
import com.blossom_project.core.common.service.GenericCrudServiceImpl;
import com.blossom_project.module.filemanager.digest.DigestUtil;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class FileServiceImpl extends GenericCrudServiceImpl<FileDTO, File> implements FileService {
  private final static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
  private final DigestUtil digestUtil;
  private final FileContentDao fileContentDao;

  public FileServiceImpl(FileDao dao, FileDTOMapper mapper, FileContentDao fileContentDao, DigestUtil digestUtil, ApplicationEventPublisher publisher,
    PluginRegistry<AssociationServicePlugin, Class<? extends  AbstractDTO>> associationRegistry) {
    super(dao, mapper, publisher, associationRegistry);
    this.fileContentDao = fileContentDao;
    this.digestUtil = digestUtil;
  }

  @Override
  @Transactional
  public FileDTO upload(MultipartFile multipartFile) throws SQLException, IOException {
    String extension = Files.getFileExtension(multipartFile.getOriginalFilename());
    if (extension != null) {
      extension = extension.toLowerCase();
    }


    FileDTO newFile = new FileDTO();
    newFile.setName(multipartFile.getOriginalFilename());
    newFile.setContentType(multipartFile.getContentType());
    newFile.setSize(multipartFile.getSize());
    newFile.setExtension(extension);
    newFile.setTags(Lists.newArrayList());

    InputStream is = multipartFile.getInputStream();

    Digest digest = new SHA256Digest();
    DigestInputStream digestStream = new DigestInputStream(is, digest);

    String hash = digestUtil.getHash(digestStream);
    String hashAlgorithm = digest.getAlgorithmName();

    newFile.setHash(hash);
    newFile.setHashAlgorithm(hashAlgorithm);

    FileDTO createdFile = this.create(newFile);

    this.fileContentDao.store(this.mapper.mapDto(createdFile), is, multipartFile.getSize());

    return createdFile;
  }

  @Override
  public InputStream download(long fileId) throws SQLException, FileNotFoundException {
    return this.fileContentDao.read(fileId);
  }
}
