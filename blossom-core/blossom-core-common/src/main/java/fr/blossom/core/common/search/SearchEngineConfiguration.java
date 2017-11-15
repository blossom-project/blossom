package fr.blossom.core.common.search;

import fr.blossom.core.common.dto.AbstractDTO;

public interface SearchEngineConfiguration<DTO extends AbstractDTO> {

  String getName();

  Class<DTO> getSupportedClass();

  String[] getFields();

  String getAlias();

  default boolean includeInOmnisearch(){
    return true;
  }

}
