package com.blossomproject.core.common.search;

import com.blossomproject.core.common.dto.AbstractDTO;
import java.util.List;

public class SearchEngineConfigurationImpl<DTO extends AbstractDTO> implements SearchEngineConfiguration<DTO> {
  private final String name;
  private final String alias;
  private final Class<DTO> supportedClass;
  private final String[] fields;

  public SearchEngineConfigurationImpl(String name, String alias,
    Class<DTO> supportedClass, List<String> fields) {
    this.name = name;
    this.alias = alias;
    this.supportedClass = supportedClass;
    this.fields = fields.toArray(new String[fields.size()]);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Class<DTO> getSupportedClass() {
    return supportedClass;
  }

  @Override
  public String[] getFields() {
    return fields;
  }

  @Override
  public String getAlias() {
    return alias;
  }
}
