package fr.mgargadennec.blossom.core.common.service;


import fr.mgargadennec.blossom.core.common.PluginConstants;
import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;


@Qualifier(value = PluginConstants.PLUGIN_SERVICE)
public interface ReadOnlyServicePlugin extends Plugin<Class<? extends AbstractDTO>> {

}
