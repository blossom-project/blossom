package fr.blossom.core.common.search;

import fr.blossom.core.common.dto.AbstractDTO;
import java.util.function.Function;
import org.springframework.core.io.Resource;

public interface IndexationEngineConfiguration<DTO extends AbstractDTO> {

  Class<DTO> getSupportedClass();

  Resource getSource();

  String getAlias();

  Function<DTO, String> getTypeFunction();

  Function<DTO, SummaryDTO> getSummaryFunction();
}
