package fr.mgargadennec.blossom.core.common.utils.privilege;


import fr.mgargadennec.blossom.core.common.PluginConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

@Qualifier(PluginConstants.PLUGIN_PRIVILEGES)
public interface PrivilegePlugin extends Plugin<String> {

  String namespace();

  String right();

  String privilege();

}
