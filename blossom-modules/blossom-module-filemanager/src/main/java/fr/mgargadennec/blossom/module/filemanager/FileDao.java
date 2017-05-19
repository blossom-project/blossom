package fr.mgargadennec.blossom.module.filemanager;

import fr.mgargadennec.blossom.core.common.dao.CrudDao;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface FileDao extends CrudDao<File> {
  List<File> getAll(String path);
}
