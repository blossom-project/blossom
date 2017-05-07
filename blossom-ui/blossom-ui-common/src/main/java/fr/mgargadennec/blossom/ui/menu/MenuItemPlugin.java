package fr.mgargadennec.blossom.ui.menu;

import fr.mgargadennec.blossom.core.common.PluginConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@Qualifier(value = PluginConstants.PLUGIN_MENU)
public interface MenuItemPlugin extends Plugin<String> {
}
