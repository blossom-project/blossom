package fr.mgargadennec.blossom.module.filemanager;

import fr.mgargadennec.blossom.core.common.event.BeforeDeletedEvent;
import fr.mgargadennec.blossom.core.common.event.DeletedEvent;
import fr.mgargadennec.blossom.module.filemanager.store.DigestUtil;
import fr.mgargadennec.blossom.module.filemanager.store.FileStore;
import fr.mgargadennec.blossom.module.filemanager.store.Folder;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class FileServiceImpl implements FileService {
  private final static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
  private final FileDao dao;
  private final FileDTOMapper mapper;
  private final FileStore store;
  private final DigestUtil digestUtil;
  private final ApplicationEventPublisher publisher;

  public FileServiceImpl(FileDao dao, FileDTOMapper mapper, FileStore store, DigestUtil digestUtil, ApplicationEventPublisher publisher) {
    this.dao = dao;
    this.mapper = mapper;
    this.store = store;
    this.digestUtil = digestUtil;
    this.publisher = publisher;
  }

  @Override
  @Transactional
  public FileDTO upload(FileDTO newFile, Folder folder, InputStream inputStream) {
    Digest digest = new SHA256Digest();
    DigestInputStream digestStream = new DigestInputStream(inputStream, digest);

    String hash = digestUtil.getHash(digestStream);
    String hashAlgorithm = digest.getAlgorithmName();

    newFile.setHash(hash);
    newFile.setHashAlgorithm(hashAlgorithm);
    newFile.setPath(folder.getPath());

    File file = this.mapper.mapDto(newFile);
    FileDTO createdFile = this.mapper.mapEntity(this.dao.create(file));

    store.add(createdFile, inputStream);

    return createdFile;
  }

  @Override
  @Transactional
  public void delete(FileDTO toDelete) {
    store.delete(toDelete);
    this.publisher.publishEvent(new BeforeDeletedEvent<>(this, toDelete));
    this.dao.delete(this.mapper.mapDto(toDelete));
    this.publisher.publishEvent(new DeletedEvent<>(this, toDelete));
  }

  @Override
  public Folder folderTree() {
    return store.folderTree();
  }

  @Override
  public Folder folder(String path) {
    return store.folder(path);
  }

  @Override
  public List<FileDTO> getAll(Folder folder) {
    return this.mapper.mapEntities(this.dao.getAll(folder.getPath()));
  }
}
