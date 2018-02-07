package com.blossom_project.core.common.service;


import com.blossom_project.core.common.PluginConstants;
import com.blossom_project.core.common.dto.AbstractDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;


@Qualifier(value = PluginConstants.PLUGIN_SERVICE)
public interface ReadOnlyServicePlugin extends Plugin<Class<? extends AbstractDTO>> {

}
