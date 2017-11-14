package fr.blossom.core.common.mapper;

import fr.blossom.core.common.PluginConstants;
import fr.blossom.core.common.entity.AbstractEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

@Qualifier(value = PluginConstants.PLUGIN_MAPPER)
public interface MapperPlugin extends Plugin<Class<? extends AbstractEntity>> {

}
