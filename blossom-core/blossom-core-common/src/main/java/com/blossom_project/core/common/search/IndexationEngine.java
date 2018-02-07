package com.blossom_project.core.common.search;


import com.blossom_project.core.common.PluginConstants;
import com.blossom_project.core.common.dto.AbstractDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

/**
 * Interface definition for creating an indexation engine for a supported dto.<br>
 * Instances of thesesare plugins so you can dynamically find one supporting a given {@code Class<? extends AbstractDTO>}
 *
 * @author Maël Gargadennnec
 */
@Qualifier(value = PluginConstants.PLUGIN_INDEXATION_ENGINE)
public interface IndexationEngine extends Plugin<Class<? extends AbstractDTO>> {

  void indexFull();

  void indexOne(long id);

  void updateOne(long id);

  void deleteOne(long dto);

}
