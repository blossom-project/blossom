package com.blossom_project.core.common.search;

import com.blossom_project.core.common.dto.AbstractDTO;
import java.util.function.Function;
import org.springframework.core.io.Resource;


/**
 * Configuration object to provide to an {@code IndexationEngineImpl} constructor.
 *
 * @author Maël Gargadennnec
 */
public interface IndexationEngineConfiguration<DTO extends AbstractDTO> {

  /**
   * Get the DTO class that this IndexationEngineConfiguration should support
   *
   * @return the supported class
   */
  Class<DTO> getSupportedClass();

  /**
   * Get the resource that holds the index source (setting / mapping)
   *
   * @return the resource
   */
  Resource getSource();

  /**
   * Get alias the engine should apply to the index
   *
   * @return the alias
   */
  String getAlias();

  /**
   * Get a function that can be applied on a dto to compute an Elasticsearch document "_type"
   *
   * @return the function
   */
  Function<DTO, String> getTypeFunction();

  /**
   * Get a function that can be applied on a dto to compute a {@link SummaryDTO} to store beside the original dto.
   *
   * @return the function
   */
  Function<DTO, SummaryDTO> getSummaryFunction();
}
