package fr.blossom.module.filemanager;

import fr.blossom.core.common.dao.GenericCrudDaoImpl;

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
    originalEntity.setContentType(modifiedEntity.getContentType());
    originalEntity.setExtension(modifiedEntity.getExtension());
    originalEntity.setSize(modifiedEntity.getSize());
    originalEntity.setTags(modifiedEntity.getTags());
    originalEntity.setHash(modifiedEntity.getHash());
    originalEntity.setHashAlgorithm(modifiedEntity.getHashAlgorithm());

    return originalEntity;
  }

}
