package com.blossom_project.core.common.mapper;

import com.blossom_project.core.common.PluginConstants;
import com.blossom_project.core.common.entity.AbstractEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

/**
 * Plugin for {@code DTOMapper}.
 *
 * @author Maël Gargadennnec
 */
@Qualifier(value = PluginConstants.PLUGIN_MAPPER)
public interface MapperPlugin extends Plugin<Class<? extends AbstractEntity>> {

}
