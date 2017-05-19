package fr.mgargadennec.blossom.module.filemanager;

import fr.mgargadennec.blossom.core.common.dao.GenericCrudDaoImpl;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class FileDaoImpl extends GenericCrudDaoImpl<File> implements FileDao {
  public FileDaoImpl(FileRepository repository) {
    super(repository);
  }

  @Override
  protected File updateEntity(File originalEntity, File modifiedEntity) {
    originalEntity.setName(modifiedEntity.getName());
    originalEntity.setPath(modifiedEntity.getPath());
    originalEntity.setType(modifiedEntity.getType());
    originalEntity.setExtension(modifiedEntity.getExtension());
    originalEntity.setSize(modifiedEntity.getSize());
    originalEntity.setTags(modifiedEntity.getTags());
    originalEntity.setHash(modifiedEntity.getHash());
    originalEntity.setHashAlgorithm(modifiedEntity.getHashAlgorithm());

    return originalEntity;
  }

  @Override
  public List<File> getAll(String path) {
    return this.repository.findAll(QFile.file.path.eq(path));
  }
}
