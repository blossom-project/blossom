package com.blossom_project.core.common.search;

import com.blossom_project.core.common.dto.AbstractDTO;

public interface SearchEngineConfiguration<DTO extends AbstractDTO> {

  String getName();

  Class<DTO> getSupportedClass();

  String[] getFields();

  String getAlias();

  default boolean includeInOmnisearch(){
    return true;
  }

}
