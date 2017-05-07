package fr.mgargadennec.blossom.core.common.search;


import fr.mgargadennec.blossom.core.common.PluginConstants;
import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

@Qualifier(value = PluginConstants.PLUGIN_INDEXATION_ENGINE)
public interface IndexationEngine extends Plugin<Class<? extends AbstractDTO>> {

  void indexFull();

  void indexOne(long id);

  void updateOne(long id);

  void deleteOne(long dto);

}
