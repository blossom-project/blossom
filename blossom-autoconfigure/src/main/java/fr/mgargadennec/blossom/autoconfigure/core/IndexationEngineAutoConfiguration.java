package fr.mgargadennec.blossom.autoconfigure.core;

import fr.mgargadennec.blossom.core.common.PluginConstants;
import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import fr.mgargadennec.blossom.core.common.search.IndexationEngine;
import fr.mgargadennec.blossom.core.common.search.IndexationEventListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;

/**
 * Created by maelg on 07/05/2017.
 */
@Configuration
@EnablePluginRegistries(IndexationEngine.class)
public class IndexationEngineAutoConfiguration {

  @Autowired
  @Qualifier(value = PluginConstants.PLUGIN_INDEXATION_ENGINE)
  private PluginRegistry<IndexationEngine, Class<? extends AbstractDTO>> registry;

  @Bean
  public IndexationEventListeners indexationEventListeners(){
    return new IndexationEventListeners(registry);
  }

}
