package com.blossomproject.module.filemanager;

import com.blossomproject.core.common.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 22/05/2017.
 */
@Repository
public interface FileContentRepository extends CrudRepository<FileContent> {
}
