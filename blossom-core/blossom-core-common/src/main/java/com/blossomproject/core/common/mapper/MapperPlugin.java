package com.blossomproject.core.common.mapper;

import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.core.common.entity.AbstractEntity;
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
