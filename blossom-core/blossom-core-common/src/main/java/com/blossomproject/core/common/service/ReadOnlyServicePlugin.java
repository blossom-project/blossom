package com.blossomproject.core.common.service;


import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.core.common.dto.AbstractDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;


@Qualifier(value = PluginConstants.PLUGIN_SERVICE)
public interface ReadOnlyServicePlugin extends Plugin<Class<? extends AbstractDTO>> {

}
