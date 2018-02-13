package com.blossomproject.ui.menu;

import com.blossomproject.core.common.PluginConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@Qualifier(value = PluginConstants.PLUGIN_MENU)
public interface MenuItemPlugin extends Plugin<String> {
}
