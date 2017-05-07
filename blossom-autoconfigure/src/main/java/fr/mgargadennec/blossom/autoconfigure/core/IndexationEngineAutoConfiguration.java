package fr.mgargadennec.blossom.autoconfigure.core;

import fr.mgargadennec.blossom.core.common.PluginConstants;
import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import fr.mgargadennec.blossom.core.common.event.CreatedEvent;
import fr.mgargadennec.blossom.core.common.event.DeletedEvent;
import fr.mgargadennec.blossom.core.common.event.UpdatedEvent;
import fr.mgargadennec.blossom.core.common.search.IndexationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by maelg on 07/05/2017.
 */
@Configuration
@EnableScheduling
@EnablePluginRegistries(IndexationEngine.class)
@PropertySource("classpath:/indexation-engine.properties")
public class IndexationEngineAutoConfiguration {

    @Autowired
    @Qualifier(value = PluginConstants.PLUGIN_INDEXATION_ENGINE)
    private PluginRegistry<IndexationEngine, Class<? extends AbstractDTO>> registry;

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    public void indexAll() {
        registry.getPlugins().stream().parallel().forEach(engine -> engine.indexFull());
    }

    @EventListener
    public void handleCreate(CreatedEvent<? extends AbstractDTO> event) {
        Class<? extends AbstractDTO> clazz = event.getDTO().getClass();
        if (registry.hasPluginFor(clazz)) {
            registry.getPluginFor(clazz).indexOne(event.getDTO().getId());
        }
    }

    @EventListener
    public void handleUpdate(UpdatedEvent<? extends AbstractDTO> event) {
        Class<? extends AbstractDTO> clazz = event.getDTO().getClass();
        if (registry.hasPluginFor(clazz)) {
            registry.getPluginFor(clazz).updateOne(event.getDTO().getId());
        }
    }

    @EventListener
    public void handleDelete(DeletedEvent<? extends AbstractDTO> event) {
        Class<? extends AbstractDTO> clazz = event.getDTO().getClass();
        if (registry.hasPluginFor(clazz)) {
            registry.getPluginFor(clazz).deleteOne(event.getDTO().getId());
        }
    }

}
