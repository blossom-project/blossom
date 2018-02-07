package com.blossom_project.core.common.search;

import com.blossom_project.core.common.dto.AbstractDTO;
import java.util.function.Function;
import org.springframework.core.io.Resource;


public class IndexationEngineConfigurationImpl<DTO extends AbstractDTO> implements
  IndexationEngineConfiguration<DTO> {

  private final Class<DTO> supportedClass;
  private final Resource source;
  private final String alias;
  private final Function<DTO, String> typeFunction;
  private final Function<DTO, SummaryDTO> summaryDTOFunction;

  public IndexationEngineConfigurationImpl(Class<DTO> supportedClass,
    Resource source, String alias, Function<DTO, String> typeFunction,
    Function<DTO, SummaryDTO> summaryDTOFunction) {
    this.supportedClass = supportedClass;
    this.source = source;
    this.alias = alias;
    this.typeFunction = typeFunction;
    this.summaryDTOFunction = summaryDTOFunction;
  }

  @Override
  public Class<DTO> getSupportedClass() {
    return supportedClass;
  }

  @Override
  public Resource getSource() {
    return source;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  @Override
  public Function<DTO, String> getTypeFunction() {
    return typeFunction;
  }

  @Override
  public Function<DTO, SummaryDTO> getSummaryFunction() {
    return summaryDTOFunction;
  }
}
